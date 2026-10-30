package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe;

import jakarta.faces.context.FacesContext;

/** Helper to install a Mockito FacesContext without relying on static mocking. */
public abstract class TestFacesContext extends FacesContext {
    private TestFacesContext() { }
    public static void install(FacesContext context) { setCurrentInstance(context); }
}
