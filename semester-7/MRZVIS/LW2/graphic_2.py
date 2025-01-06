from PIL import Image
import os

def resize_image(image_path, output_folder, size_range, step):
    # Create output folder if it doesn't exist
    os.makedirs(output_folder, exist_ok=True)

    # Open the image
    with Image.open(image_path) as img:
        original_size = img.size
        print(f"Original size: {original_size}")

        # Resize the image in the specified range
        for size in range(size_range[0], size_range[1] + 1, step):
            # Resize while maintaining aspect ratio
            img_resized = img.resize((size, int(size * img.height / img.width)))
            # Save the resized image
            resized_image_path = os.path.join(output_folder, f"o_{size}.png")
            img_resized.save(resized_image_path)
            print(f"Saved resized image to: {resized_image_path}")

if __name__ == "__main__":
    # Path to the input image
    image_path = "noisy_letters/o_1.png"  # Change this to your image path
    # Output folder for resized images
    output_folder = "resized_images_noisy"
    # Size range (min size, max size) and step
    size_range = (21, 256)  # Change these values as needed
    step = 10  # Change this value as needed

    resize_image(image_path, output_folder, size_range, step)
