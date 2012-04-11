
package loci.formats.utests.xml;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.InputStream;

import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.ome.OMEXMLMetadata;
import loci.formats.services.OMEXMLService;

import ome.xml.model.Image;
import ome.xml.model.OME;
import ome.xml.model.Pixels;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/test/loci/formats/utests/Upgrade200809Test.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/test/loci/formats/utests/Upgrade200809Test.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @author Colin Blackburn <cblackburn at dundee dot ac dot uk>
 */
public class Upgrade200809Test {

  private static final String XML_FILE = "2008-09.ome";

  private OMEXMLService service;
  private String xml;
  private OMEXMLMetadata metadata;
  private OME ome;

  @BeforeMethod
  public void setUp() throws Exception {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(OMEXMLService.class);

    InputStream s = Upgrade200809Test.class.getResourceAsStream(XML_FILE);
    byte[] b = new byte[s.available()];
    s.read(b);
    s.close();

    xml = new String(b);
    metadata = service.createOMEXMLMetadata(xml);
    ome = (OME) metadata.getRoot();
  }

  @Test
  public void getOMEXMLVersion() throws ServiceException {
    assertEquals("2011-06", service.getOMEXMLVersion(metadata));
  }

  @Test
  public void validateUpgrade() throws ServiceException {
    assertEquals(1, ome.sizeOfImageList());
    Image image = ome.getImage(0);
    Pixels pixels = image.getPixels();
    assertNotNull(pixels);
  }

  @Test
  public void testChannelColor() {
    // OME:Channel, Color now use new colour representation and is required
    assertEquals(-1, metadata.getChannelColor(0, 0).intValue());
  }

}
