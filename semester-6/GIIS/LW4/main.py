import pyvista as pv

class Figures:

    default_colors = ( "ff0000", "28e5da", "0000ff", "ffff00", "c8bebe", "f79292", "fffff0", "f18c1d",
                       "23dcaa", "d785ec", "9d5b13", "e4e0b1", "894509", "af45f5", "fff000" )
    default_meshes = ( pv.Cube, pv.Sphere, pv.Cone, pv.Cylinder, pv.Circle, pv.Arrow )
    
    def __init__(self, meshes=default_meshes, colors=default_colors) -> None:
        self.plotter = pv.Plotter()
        for i, mesh in enumerate(meshes):
            added = self.plotter.add_mesh(mesh(), name=str(mesh), color=colors[i])
            added.SetVisibility(False)
            self.plotter.add_checkbox_button_widget( added.SetVisibility, value=False, position=(5, 70 * i), color_on=colors[i] )
    
    def show(self):
        self.plotter.show()

Figures().show()
