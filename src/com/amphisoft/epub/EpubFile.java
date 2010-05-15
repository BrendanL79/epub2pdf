/*
epub2pdf, version 0.5 - Copyright 2010 Brendan C. LeFebvre

This file is part of epub2pdf.

epub2pdf is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

epub2pdf is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with epub2pdf.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.amphisoft.epub;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Encapsulates file & filesystem operations associated with a parent {@link Epub}.
 * @author brendanl
 *
 */
class EpubFile {

    /**
     * System path referencing the .epub file.
     */
    public final String epubFilePath;

    /**
     * The filesystem location of the EPUB archive represented by this object.
     * The archive may or may not actually exist, depending on context.
     */
    private File file;

    /**
     * The ZIP archive holding the file constituents of this EPUB.
     */
    private ZipFile zFile;

    /**
     * The entries of the ZIP archive that holds the file constituents of this EPUB.
     */
    private List<ZipEntry> zEntries;

    EpubFile(String s) {
        epubFilePath = s;
        file = new File(s);
    }

    EpubFile(File f) {
        epubFilePath = f.getAbsolutePath();
        file = new File(f,"");
        refreshZipEntryList();
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean canRead() {
        return file.canRead();
    }

    public boolean unzipTo(File destDir) {
        boolean allFilesUnpacked = true;
        try {
            for (ZipEntry zE : zEntries) {
                //println("*** " + zE.getName());
                if (zE.isDirectory()) {
                    File newDir = new File(destDir.getCanonicalPath(), zE.getName());
                    newDir.mkdirs();
                } else {
                    InputStream iS = zFile.getInputStream(zE);
                    String zName = zE.getName();

                    // Auto-create parent dirs
                    // need to do this because valid EPUB may not have zip entries for each directory proper
                    int leafStartIdx = zName.lastIndexOf("/") + 1;
                    String leaflessZName = zName.substring(0, leafStartIdx);
                    File newDir = new File(destDir.getCanonicalPath(), leaflessZName);
                    if (newDir.mkdirs()) {
                        //println("Auto-created " + leaflessZName);
                    }
                    // End auto-dir-create digression

                    File newFile = new File(destDir.getCanonicalPath(), zE.getName());
                    newFile.delete();
                    if (newFile.createNewFile()) {
                        BufferedOutputStream bOS =
                            new BufferedOutputStream(
                            new FileOutputStream(newFile));
                        int b = iS.read();
                        while (b != -1) {
                            bOS.write(b);
                            b = iS.read();
                        }
                        iS.close();
                        bOS.close();
                    } else {
                        allFilesUnpacked = false;
                        System.err.println("FAILED to unpack file.");
                        System.err.println("Error creating " + zE.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return allFilesUnpacked;
    }

    public String getDeclaredPath() {
        return epubFilePath;
    }

    public String getCanonicalPath() throws IOException {
        return file.getCanonicalPath();
    }


    private void refreshZipEntryList() {
        zEntries = new ArrayList<ZipEntry>();

        try {
            zFile = new ZipFile(file);

            Enumeration<? extends ZipEntry> zEntryEnum =
                zFile.entries();

            while (zEntryEnum.hasMoreElements()) {
                zEntries.add(zEntryEnum.nextElement());
            }
        } catch (IOException e) {
            // actual .epub file might not have been created yet
            // IOEx here should not be considered a fatal error.
            // Just exit quietly with the entry list left empty.
        }

    }

    public void printFileList() {
        for (ZipEntry zE : zEntries) {
            printZipEntry(zE);
        }
    }


    private void printZipEntry(ZipEntry zE) {
        if (zE.isDirectory())
            System.out.print('D');
        else
            System.out.print(' ');
        System.out.print(' ');
        System.out.println(zE.getName());
    }

    @Override
    public String toString() {
        return "EpubFile:" + epubFilePath;
    }
}
