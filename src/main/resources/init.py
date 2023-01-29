import bpy, mathutils
from bpy import context
from bpy_extras.io_utils import ExportHelper
from os.path import dirname, join

bl_info = {
    "name": "Nier:Automata Animation Exporter",
    "category": "Import-Export",
    "blender": (2, 80, 0),
}

class LocationBones(bpy.types.Operator, ExportHelper):
    
    bl_idname = "animation.animation"  
    bl_label = "Export Nier:Automata Animation File"         
    bl_options = {'REGISTER', 'UNDO'}
    
    filename_ext = ".ran"

    def execute(self, context):
        if bpy.context.object and bpy.data.filepath:
            fname = bpy.context.object.name + ".ran"
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
            f.close()
        return {'FINISHED'}

def menu_func(self, context):
    self.layout.operator(LocationBones.bl_idname)

def register():
    bpy.utils.register_class(LocationBones)

def unregister():
    bpy.utils.unregister_class(LocationBones)
    
if __name__ == '__main__':
    register()