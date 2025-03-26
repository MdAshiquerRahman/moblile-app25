from django.db import models
from django.contrib.auth.models import User

class ImagePost(models.Model):
    title = models.CharField(max_length=255)
    image = models.ImageField(upload_to='images/')
    uploaded_by = models.ForeignKey(User, on_delete=models.CASCADE)
    uploaded_at = models.DateTimeField(auto_now_add=True)
    likes = models.ManyToManyField(User, related_name='image_likes', blank=True)
    dislikes = models.ManyToManyField(User, related_name='image_dislikes', blank=True)

    def __str__(self):
        return self.title


class ImageComment(models.Model):
    image = models.ForeignKey(ImagePost, on_delete=models.CASCADE, related_name='comments')
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    text = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
