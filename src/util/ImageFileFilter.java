/*
 * ImageFileFilter.java
 *
 *
 *  Copyright (C) 2007 COMP5425 Multimedia Storage, Retrieval and Delivery
 *  The School of Information Technology
 *  The University of Sydney
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Yuezhong Zhang  SID:305275631
 * @author eadams
 * @author denny
 */

public class ImageFileFilter extends FileFilter implements java.io.FileFilter {

    /** Creates a new instance of ImageFileFilter */
    private String exts[] = {".jpg", ".png", ".jpeg", ".gif"};
    private boolean acceptFolder = true;

    public ImageFileFilter(boolean folder) {
        this.acceptFolder = folder;
    }

    public ImageFileFilter() {
    }

    public boolean accept(File file) {
        if (file.isDirectory() && acceptFolder) {
            return true;
        }
        if (file.isFile()) {
            String name = file.getName().toLowerCase();
            for (String ext : exts) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public String getDescription() {
        return "All Image Types";
    }
}
