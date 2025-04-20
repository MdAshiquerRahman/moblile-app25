from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .api_views import VideoViewSet, VideoCommentViewSet

router = DefaultRouter()
router.register(r'upload-videos', VideoViewSet, basename='video')  # Changed to hyphen
router.register(r'video-comments', VideoCommentViewSet, basename='video-comment')

urlpatterns = [
    path('', include(router.urls)),
    # This is now redundant since we added url_path to the action decorator
     path('liked-videos/', VideoViewSet.as_view({'get': 'liked_videos'}), name='liked-videos'),
]