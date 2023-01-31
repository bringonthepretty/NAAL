package com.wah.naal;

import com.wah.naal.converter.RawMotionToMotionConverter;
import com.wah.naal.exception.ParseException;
import com.wah.naal.io.FileIO;
import com.wah.naal.model.motionfile.motion.Motion;
import com.wah.naal.model.rawanimationfile.rawanimation.RawAnimation;
import com.wah.naal.model.rawanimationfile.rawanimation.builder.RawAnimationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    public static FileIO fileIO = FileIO.getInstance();
    public static RawAnimationBuilder rawAnimationBuilder = RawAnimationBuilder.getInstance();
    public static RawMotionToMotionConverter rawMotionConverter = RawMotionToMotionConverter.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        fileIO.loadAllRawAnimationFiles().forEach((name, data) -> {
            try {
                RawAnimation rawAnimation = rawAnimationBuilder.build(data, name);
                Motion motion = rawMotionConverter.convert(rawAnimation);
                fileIO.saveMotion(motion, name);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        });
    }
}