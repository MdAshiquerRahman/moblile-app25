# Generated by Django 5.2 on 2025-04-17 15:36

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0003_rename_bio_myuser_userid'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='myuser',
            name='UserId',
        ),
    ]
