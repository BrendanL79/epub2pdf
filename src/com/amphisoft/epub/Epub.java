/*
epub2pdf, version 0.3 - Copyright 2010 Brendan C. LeFebvre

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

import java.io.File;
import java.io.IOException;

import com.amphisoft.epub.metadata.Container;
import com.amphisoft.epub.metadata.Ncx;
import com.amphisoft.epub.metadata.Opf;
/**
 * Represents a single document in the EPUB format.<br />
 * The document represented by an instance of this object may be referred to simply as "the EPUB" or "this EPUB" in the documentation for this and related classes.
 * @author brendanl
 *
 */
public class Epub {

    private EpubFile file;

    private Container container;
    private Opf opf;
    private Ncx ncx;

    /**
     * Folder into which the EPUB is unzipped for parsing
     */
    private File tempDir;

    /**
     * Points to the folder containing the OPF metadata file. This folder, located within tempDir, serves as the root for all relative paths in OPF and NCX metadata.
     */
    private File contentRoot;



    private Epub() {

    }

    private Epub(File f) throws IOException {
        setFile(f);
    }

    private Epub(String s) throws IOException {
        setFile(new File(s));
    }

    public void setFile(File f) throws IOException {
        file = new EpubFile(f);
        if (file.exists() && file.canRead()) {
            populateMemberFields();
        }

    }

    private void setFile(String s) throws IOException {
        setFile(new File(s));
    }

    Container getContainer() {
        return container;
    }

    public Opf getOpf() {
        return opf;
    }

    public Ncx getNcx() {
        return ncx;
    }

    /**
     * Creates an Epub object from a pre-existing EPUB file.
     * @param filename path to the EPUB file
     * @return a new Epub instance that will, in theory, produce an EPUB file identical to the one used for input.
     * @throws IOException if the file read goes awry
     */
    public static Epub fromFile(String filename) throws IOException {
        Epub epub = new Epub();
        epub.setFile(filename);
        return epub;
    }

    private void populateMemberFields() throws IOException {
        tempDir = new File(System.getProperty("java.io.tmpdir"),"_e2p-"+System.currentTimeMillis());
        if (tempDir.mkdirs() == false) {
            throw new IOException("Failed to create temp dir " + tempDir.getCanonicalPath());
        }

        file.unzipTo(tempDir);

        File containerFile = new File(tempDir,"META-INF/container.xml");
        if (!(containerFile.exists())) {
            throw new IOException("META-INF/container.xml unexpectedly missing");
        }
        container = Container.fromFile(containerFile.getCanonicalPath());

        File opfFile = new File(tempDir,container.rootMetadataPath);
        if (!(opfFile.exists())) {
            throw new IOException(
                "OPF metadata file " +
                opfFile.getCanonicalPath() +
                " unexpectedly missing");
        }
        opf = Opf.fromFile(opfFile.getCanonicalPath());
        contentRoot = new File(opfFile.getParent());

        File ncxFile = new File(contentRoot,opf.tocHref());
        if (!(ncxFile.exists())) {
            throw new IOException(
                "NCX navigation file " +
                ncxFile.getCanonicalPath() +
                " unexpectedly missing");
        }
        ncx = Ncx.fromFile(ncxFile.getCanonicalPath());
    }

    public void cleanup() {
        cleanRecursively(tempDir);
    }

    private void cleanRecursively(File target) {
        if (target.isDirectory()) {
            for (File child : target.listFiles()) {
                cleanRecursively(child);
            }
        }
        if (!(target.delete())) {
            target.deleteOnExit();
        }
    }
    public boolean extractTo(File destDir) {
        return file.unzipTo(destDir);
    }

    /**
     *
     * @return a String expressing the canonical path of the EPUB file represented by this object.
     */
    public String getEpubPath() {
        try {
            return file.getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void setContentRoot(File contentRoot) {
        this.contentRoot = contentRoot;
    }

    public File getContentRoot() {
        return contentRoot;
    }

    public void setOpf(Opf opf) {
        this.opf = opf;
    }

    public void setNcx(Ncx ncx) {
        this.ncx = ncx;
    }
}
