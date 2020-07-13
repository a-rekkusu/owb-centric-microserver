import org.apache.peeco.api.Matching;
import org.apache.peeco.impl.PeecoUtils;
import org.junit.Assert;
import org.junit.Test;

public class PeecoUtilsTest
{
    @Test
    public void testGetMatchingHandlerWildcard()
    {
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello3/*", "/hello3/id=2342"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello/*", "/hello/?lang=de-de"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello3/world/*", "/hello3/world/?id=2342"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello/world*", "/hello/world/?lang=de-de"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "*.jpg", "/hello/blub.jpg?lang=de-de"));
    }

    @Test
    public void testGetMatchingHandlerExact(){
        Assert.assertTrue(PeecoUtils.isMatching(Matching.EXACT, "/hello/", "/hello/?lang=de-de"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.EXACT, "/hello", "/hello?lang=de-de"));
        Assert.assertFalse(PeecoUtils.isMatching(Matching.EXACT, "/hello3", "/hello?lang=de-de"));
        Assert.assertFalse(PeecoUtils.isMatching(Matching.EXACT, "/hello3/", "/hello?lang=de-de"));
    }
}