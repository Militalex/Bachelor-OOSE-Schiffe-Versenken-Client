package de.gitlab.kaisoenmalex.api.util;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This class can manage an amount (Menge) of images and there rotations.
 * All images were merged together (with individual rotation) to create one image.
 */
public class ImageManager {

    /**
     * A Hashmap containing all Images from textures folder. The Hashmap maps filename (with .png) and the corresponding Image.
     */
    public static final HashMap<String, Image> IMAGE_MAP = new HashMap<>();

    //Render Pipeline
    /**
     * List of all imageKeys which were later merged together.
     */
    private final List<String> imageKeys = new ArrayList<>();
    /**
     * List of corresponding rotations.
     */
    private final List<Rotation> imageRot = new ArrayList<>();

    /**
     * Rendered Image
     */
    @Expose(serialize = false, deserialize = false)
    @Nullable private Image image;

    /**
     * Creates an empty ImageManager (imageKeys and imageRot are empty).
     */
    public ImageManager(){ }

    /**
     * @param imageKey ImageKey (Note: The key have to exists in IMAGE_MAP)
     */
    public ImageManager(String... imageKey){
        this(Arrays.asList(imageKey));
    }

    /**
     * @param imageKeys ImageKeys (Note: All list elements have to exists in IMAGE_MAP)
     */
    public ImageManager(List<String> imageKeys) {
        imageKeys.forEach(this::addKey);
    }

    /**
     * Adds an image (via key) to render pipeline.
     * @param key ImageKey (Note: The key have to exists in IMAGE_MAP)
     * @param rot Individual Rotation.
     */
    public void addKey(@NotNull String key, Rotation rot){
        if (!IMAGE_MAP.containsKey(key)) throw new IllegalArgumentException("Key is not in Image Map.");

        imageKeys.add(key);
        imageRot.add(rot);
    }

    /**
     * Adds an image (via key) to render pipeline (Individual Rotation is 0).
     * @param key ImageKey (Note: The key have to exists in IMAGE_MAP)
     */
    public void addKey(@NotNull String key){
        addKey(key, Rotation.NORTH);
    }

    /**
     *  Removes image key and corresponding rotation data.
     */
    public void removeKey(String key){
        int pos = imageKeys.indexOf(key);
        imageKeys.remove(key);
        imageRot.remove(pos);
    }

    /**
     * Clears ImageManager. Image is after clear() null.
     */
    public void clear(){
        imageKeys.clear();
        imageRot.clear();
    }

    /**
     * @return Returns rendered image.
     */
    public Image getImage() {
        renderImage();
        return image;
    }

    /**
     * Render all images in pipeline with their individual rotations.
     */
    private void renderImage(){
        if (imageKeys.isEmpty()) return;

        List<Image> list = new ArrayList<>();

        for (int i = 0; i < imageKeys.size(); i++){
            final Rotation rot = imageRot.get(i);
            Image image = IMAGE_MAP.get(imageKeys.get(i));

            if (rot != Rotation.NORTH){
                image = ImageUtil.rotateImage(image, rot.getDegrees());
            }

            list.add(image);
        }

        this.image = ImageUtil.mergeImages(list);
    }
}
