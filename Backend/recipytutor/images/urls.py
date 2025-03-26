from django.urls import path
from . import views

urlpatterns = [
    path('', views.image_list, name='image_list'),
    path('upload/', views.upload_image, name='upload_image'),
    path('<int:image_id>/comment/', views.add_image_comment, name='add_image_comment'),
]
