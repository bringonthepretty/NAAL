package com.wah.naal;

import com.wah.naal.converter.Converter;
import com.wah.naal.io.FileIO;
import com.wah.naal.model.bvhfile.bvh.Bvh;
import com.wah.naal.model.bvhfile.bvh.builder.BvhBuilder;
import com.wah.naal.model.motionfile.motion.Motion;

public class Application {

    public static Converter converter = Converter.getInstance();
    public static FileIO fileIO = FileIO.getInstance();

    public static void main(String[] args) {
        fileIO.loadAllBvh().forEach((name, data) -> {
            Bvh bvh = BvhBuilder.getInstance().build(data, name);
            Motion motion = converter.convert(bvh);
            fileIO.saveMotion(motion, name);
        });
    }
}