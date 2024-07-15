package com.g1.ai_image_g1.model;

public class ImageModel {

    private String prompt;
    private String steps;
    private static final String PREPARE_PROMPT = "(RAW photo, best quality), (realistic, photo-realistic:1.3), masterpiece, " +
            "an extremely delicate and beautiful, extremely detailed, CG, unity , 2k wallpaper, Amazing, " +
            "finely detail, extremely detailed CG unity 8k wallpaper, huge filesize, ultra-detailed, highres, " +
            "absurdres, soft light, beautiful detailed girl, detailed fingers, extremely detailed eyes and face, " +
            "beautiful detailed nose, detailed face, beautiful detailed eyes, light on face, looking at viewer, " +
            "1girl, cute, young, mature face, realistic face, realistic body, beautiful detailed thigh, ";
    private static final String PREPARE_NEGATIVE = "(nude:2), (nsfw:2), breast, (naked:2), paintings, sketches, (worst quality:2), (low quality:2), " +
            "(normal quality:2), lowres, ((monochrome)), ((grayscale)), skin spots, acnes, skin blemishes, age spot, " +
            "glans, extra fingers, fewer fingers, ((watermark:2)), (white letters:1), nipples, bad anatomy, " +
            "bad hands, text, error, missing fingers, missing arms, missing legs, extra digit, fewer digits, cropped, " +
            "worst quality, jpeg artifacts, signature, watermark, username, bad feet, {Multiple people}, blurry, " +
            "poorly drawn hands, poorly drawn face, mutation, deformed, extra limbs, extra arms, extra legs, " +
            "malformed limbs, fused fingers, too many fingers, long neck, cross-eyed, mutated hands, " +
            "polar lowres, bad body, bad proportions, gross proportions, wrong feet bottom render, " +
            "abdominal stretch, briefs, knickers, kecks, thong, {{fused fingers}}, {{bad body}}, " +
            "EasyNegative, bad proportion body to legs, wrong toes, extra toes, missing toes, weird toes, 2 body, pussy, 2 upper, 2 lower, 2 head, 3 hand, 3 feet, " +
            "extra long leg, super long leg, mirrored image, mirrored noise, aged up, old";
    private static final String SAMPLER_INDEX = "DPM++ SDE Karras";
    private static final String SAMPLER_NAME = "DPM++ SDE Karras";
    private static final String IMAGE_HEIGHT = "768";
    private static final String IMAGE_WIDTH = "512";
    //private String ImageUrl;


    public ImageModel() {
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getPreparePrompt() {
        return PREPARE_PROMPT;
    }

    public String getPrepareNegative() {
        return PREPARE_NEGATIVE;
    }

    public String getHeight() {
        return IMAGE_HEIGHT;
    }

    public String getWidth() {
        return IMAGE_WIDTH;
    }

    public String getSamplerIndex() {
        return SAMPLER_INDEX;
    }

    public String getSamplerName() {
        return SAMPLER_NAME;
    }

    //public void setImageUrl(String imageUrl) {
        //ImageUrl = imageUrl;
    //}
}
