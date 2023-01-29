package com.wah.naal;

import com.wah.naal.converter.RawMotionToMotionConverter;
import com.wah.naal.io.FileIO;
import com.wah.naal.model.motionfile.motion.Motion;
import com.wah.naal.model.rawanimationfile.rawanimation.RawAnimation;
import com.wah.naal.model.rawanimationfile.rawanimation.builder.RawAnimationBuilder;

public class Application {

    public static FileIO fileIO = FileIO.getInstance();
    public static RawAnimationBuilder rawAnimationBuilder = RawAnimationBuilder.getInstance();
    public static RawMotionToMotionConverter rawMotionConverter = RawMotionToMotionConverter.getInstance();

    public static void main(String[] args) {
        fileIO.loadAllRawAnimationFiles().forEach((name, data) -> {
            RawAnimation rawAnimation = rawAnimationBuilder.build(data, name);
            Motion motion = rawMotionConverter.convert(rawAnimation);
            fileIO.saveMotion(motion, name);
        });
    }
}