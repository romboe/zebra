package zebra;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import zebra.model.House;
import zebra.model.ModelHelper;
import zebra.model.Nation;

public class ModelHelperTest {

	@Test
	public void testDistinct() {
		House h1 = new House(null, null, new Nation("a"), null, null, null);
		House h2 = new House(null, null, new Nation("a"), null, null, null);
		assertFalse(ModelHelper.areDistinct(h1, h2));

		h2 = new House(null, null, new Nation("b"), null, null, null);
		assertTrue(ModelHelper.areDistinct(h1, h2));

		h2 = new House(null, null, null, null, null, null);
		assertTrue(ModelHelper.areDistinct(h1, h2));
	}

	@Test
	public void testDistinct2() {
		House h1 = new House(null, 1, new Nation("a"), null, null, null);
		House h2 = new House(null, 2, new Nation("a"), null, null, null);
		assertFalse(ModelHelper.areDistinct(h1, h2));

		h1 = new House("rot", 1, new Nation("a"), null, null, null);
		h2 = new House(null, 1, null, null, null, null);
		assertTrue(ModelHelper.areDistinct(h1, h2));
	}
}
