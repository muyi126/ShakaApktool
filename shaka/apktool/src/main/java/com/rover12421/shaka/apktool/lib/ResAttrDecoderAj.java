/**
 *  Copyright 2015 Rover12421 <rover12421@163.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.rover12421.shaka.apktool.lib;

import brut.androlib.res.data.ResResSpec;
import brut.androlib.res.data.ResResource;
import brut.androlib.res.data.value.ResFileValue;
import com.rover12421.shaka.lib.LogHelper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.io.File;

/**
 * Created by rover12421 on 8/9/14.
 * brut.androlib.res.decoder.ResAttrDecoder
 */
@Aspect
public class ResAttrDecoderAj {

    private String getResSpecName(String name) {
        /**
         * 过滤包名
         */
        int index = name.indexOf(":");
        if (index > 0) {
            name = name.substring(index+1);
        }
        return name;
    }

    @AfterReturning(pointcut = "execution(* brut.androlib.res.decoder.ResAttrDecoder.decode(..))" +
            "&& args(type, value, rawValue, attrResId)", returning = "ret")
    public void decode_afterRetruning(int type, int value, String rawValue, int attrResId, String ret) throws Exception {
        if (ResFileDecoderAj.DonotRecord) {
            return;
        }
        ResResSpec spec = ResTypeSpecAj.AllSpecs.get(value);
        if (spec != null) {
            //查找到ResResSpec
            String oldName = spec.getName();
            int index = ret.indexOf("/");   // type/name.ext
            if (index > 0) {
                String newName = ret.substring(index + 1);
                newName = getResSpecName(newName);
                if (!oldName.equals(newName)) {
                    LogHelper.warning("Rename ResResSpec " + oldName + " to " + newName);
                    ResResSpecAj.setName(spec, newName);
                    ResTypeSpecAj.addSpecToResType(spec);

                    /**
                     * 需要再次Decode
                     * 可能有些值已经写入到文件了,需要重新Decode来纠正
                     */
                    ResFileDecoderAj.NeedReDecodeFiles = true;

                    ResResource res = ResTypeAj.MultopleResFileValue.get(spec.getId().id);
                    if (res != null) {
                        ResFileValue fileValue = (ResFileValue) res.getValue();
                        String mapPath = AndrolibAj.metaInfo.getDecodeFileMap(fileValue.getPath());
                        File outfile = new File(AndrolibResourcesAj.getOutDir().getAbsolutePath() + File.separator +  mapPath);
                        System.out.println(outfile.exists()+"");
                        if (outfile.exists()) {
                            String fileName = outfile.getName();
                            int extIndex = fileName.lastIndexOf(".");
                            String ext = "";
                            if (extIndex > 0) {
                                ext = fileName.substring(extIndex);
                            }
                            File renamefile = new File(outfile.getParent() + File.separator + newName + ext);
                            outfile.renameTo(renamefile);
                            System.out.println(renamefile.getAbsoluteFile());
                            String newMapFile = renamefile.getAbsolutePath().substring(AndrolibResourcesAj.getOutDir().getAbsolutePath().length()+1);
                            AndrolibAj.metaInfo.addDecodeFileMap(fileValue.getPath(), newMapFile);
                            LogHelper.warning("Rename resource file " + fileValue.getPath() + " to " + newMapFile);
                        }
                    }else {
                        System.out.println("res null");
                    }
                }
            }

        }
    }

}
