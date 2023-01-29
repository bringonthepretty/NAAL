import bpy
from bpy_extras.io_utils import ExportHelper
import os

bl_info = {
    "name" : "Nier:Automata Animation Exporter",
    "blender" : (2, 80, 0),
    "location" : "",
    "warning" : "",
    "category" : "Import-Export"
}

class ExportMotFile(bpy.types.Operator, ExportHelper):
    """Export a Nier Animation Animation file"""
    bl_idname = "export_animation.ran"
    bl_label = "Export"
    bl_options = {'UNDO'}

    filename_ext = ".ran"
    filter_glob: bpy.props.StringProperty(default="*.ran", options={'HIDDEN'})

    def execute(self, context):
        if bpy.context.object and bpy.data.filepath:
            fname = self.filepath
            fpath = join( dirname( bpy.data.filepath ),  fname )
            with open( fpath, "w" ) as f:
                bone = bpy.context.active_object.pose.bones[0]
                f.write(str("location") + ':')
                for frame in range(context.scene.frame_start, context.scene.frame_end+1):
                    context.scene.frame_set(frame)
                    f.write(str(bone.location))
                f.write( str( "\n" ) )
                for bone in bpy.context.active_object.pose.bones[:]:
                    f.write( str( bone.name ) + ':' )
                    for frame in range(context.scene.frame_start, context.scene.frame_end+1):
                        context.scene.frame_set(frame)
                        f.write( str( bone.rotation_quaternion) )
                    f.write( str( "\n" ) )

        self.report({'INFO'}, "Exported ran file")

        return {'FINISHED'}

def exportMenuAdditions(self, context):
    self.layout.operator(ExportMotFile.bl_idname, text="Nier:Automata animation (.ran)")

def register():
    bpy.utils.register_class(ExportMotFile)

    bpy.types.TOPBAR_MT_file_export.append(exportMenuAdditions)

def unregister():
    bpy.utils.unregister_class(ExportMotFile)

    bpy.types.TOPBAR_MT_file_export.remove(exportMenuAdditions)