# Instantly create images for instagram

#### Created this because I had a folder of images I wanted to auto size for instagram

Clone project, package your jar, and run with this command

```
java -jar instant_images-1.0-SNAPSHOT.jar images/ 255 255 255
```

'images/' is your directory with all your images, it will be scanned recursively until all valid images are found

```
These are the current supported file types '.jpg', '.jpeg', '.png', '.gif'
```

(255, 255, 255) is the background color of the new images in (red, green, blue) format

If your image is not transparent you won't see the background color, just fyi

Converted images are sized as 600w x 600h

If your image(s) are larger they will maintain ratio to fit inside the 600w x 600h dimensions

If your image(s) are smaller than 600w x 600h they will not be resized and will be placed in the center

A "dst/" directory will be automatically created containing the converted images
