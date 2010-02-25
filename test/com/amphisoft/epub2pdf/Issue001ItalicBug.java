package com.amphisoft.epub2pdf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Issue001ItalicBug {

	private File tempDir;
	private Converter c;
	
	@Before
	public void setUp() throws Exception {
        setupTempDir();
        setupConverter();
	}

	@Test
	public void testItalicBug() throws Exception {
		c.convert("test/data/issue001-italic-bug.epub");
		fail("Not yet implemented");
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	private void setupConverter() throws Exception {
		c = new Converter();
		Properties p = Converter.getProperties();
		p.setProperty("output.dir", tempDir.getCanonicalPath());
		c.applyProperties(p);
	}
	
	private void setupTempDir() throws Exception {
		tempDir = new File(System.getProperty("java.io.tmpdir"),"_e2p_test-"+System.currentTimeMillis());
        if (tempDir.mkdirs() == false) {
            throw new IOException("Failed to create temp dir " + tempDir.getCanonicalPath());
        }		
	}

}
