import torch
import numpy as np
import torchvision
from torchvision import transforms
import torchvision.models as models
from PIL import Image
from io import BytesIO
import requests as req


class ImageClassifier(torch.nn.Module):
    def __init__(self, class_num, hidden_dim=256, dropout=.1):
        super(ImageClassifier, self).__init__()
        self.backbone = torchvision.models.resnet34(pretrained=True)
        self.feature_dim = self.backbone.fc.in_features
        self.backbone.fc = IdentityLayer()
        self.outlayer = torch.nn.Sequential(
            torch.nn.Linear(self.feature_dim, hidden_dim),
            torch.nn.ReLU(),
            torch.nn.Dropout(dropout),
            torch.nn.Linear(hidden_dim, class_num)
        )

    def forward(self, processed_img):
        feature_vec = self.backbone(processed_img)
        prediction = self.outlayer(feature_vec)
        return prediction


class IdentityLayer(torch.nn.Module):
    def __init__(self):
        super(IdentityLayer, self).__init__()

    def forward(self, x):
        return x

model = models.resnet18()
model.avgpool = torch.nn.AdaptiveAvgPool2d(1)
# model.load_state_dict(model_zoo.load_url(model_urls[model_name]))
num_ftrs = model.fc.in_features
model.fc = torch.nn.Linear(num_ftrs, 20)
model.load_state_dict(torch.load('../models/resnet18/model-2.pth', map_location=torch.device('cpu')))

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model.to(device)
model.train(False)
model_scenery = ImageClassifier(6)
model_scenery.load_state_dict(torch.load('../models/image_classifier.pth', map_location=torch.device('cpu')))
model_scenery = model_scenery.to(device)
model_scenery.eval()

fig_size = (256, 256)
fig_mean = [0.485, 0.456, 0.406]
fig_std = [0.229, 0.224, 0.225]
object_categories = ['aeroplane', 'bicycle', 'bird', 'boat',
                     'bottle', 'bus', 'car', 'cat', 'chair',
                     'cow', 'diningtable', 'dog', 'horse',
                     'motorbike', 'person', 'pottedplant',
                     'sheep', 'sofa', 'train', 'tvmonitor']
scenery_categories = ['buildings', 'forest', 'glacier', 'mountain', 'sea', 'street']


def infer(image_name):
    mean = [0.457342265910642, 0.4387686270106377, 0.4073427106250871]
    std = [0.26753769276329037, 0.2638145880487105, 0.2776826934044154]
    transformations_test = transforms.Compose([transforms.Resize(330),
                                               transforms.ToTensor(),
                                               transforms.Normalize(mean=mean, std=std),
                                               ])

    pred_process_pipe = torchvision.transforms.Compose([
        torchvision.transforms.Resize(fig_size),
        torchvision.transforms.ToTensor(),
        torchvision.transforms.Normalize(fig_mean, fig_std)
    ])
    # '''
    response = req.get(image_name)
    image = Image.open(BytesIO(response.content),
                       mode='r').convert('RGB')
    # '''
    # image = Image.open(image_name, mode='r').convert('RGB')
    images = transformations_test(image).unsqueeze(0).to(device)
    input_img = pred_process_pipe(image).unsqueeze(0).to(device)
    output = model(images)
    output_scenery = model_scenery(input_img)
    # print(output.shape)
    # result = list()
    object_list = list()
    scenery_list = list()
    categories = {}
    for i in range(output.shape[0]):
        for j in range(output.shape[1]):
            if output[i][j] >= 0:
                object_list.append(object_categories[j])
    # return output.detach().numpy().tolist()
    for i in range(output_scenery.shape[0]):
        for j in range(output_scenery.shape[1]):
            if output_scenery[i][j] >= 2.5:
                scenery_list.append(scenery_categories[j])
    # print(output,output_scenery)
    if len(object_list) > 0:
        categories['object_categories'] = ','.join(object_list) 
    if len(scenery_list) > 0:
        categories['scenery_categories'] = ','.join(scenery_list)
    return categories


# if __name__ == '__main__':
#     print(infer('C:\\Users\\Lenovo\\Pictures\\Saved Pictures\\hill.jpg'))