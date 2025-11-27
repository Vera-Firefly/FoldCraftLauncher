package com.mio.manager

import android.content.Context
import com.mio.data.Renderer
import com.tungsten.fcl.R
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fclauncher.utils.FCLPath

object RendererManager {
    lateinit var RENDERER_GL4ES: Renderer
    lateinit var RENDERER_VIRGL: Renderer
    lateinit var RENDERER_VGPU: Renderer
    lateinit var RENDERER_ZINK: Renderer
    lateinit var RENDERER_FREEDRENO: Renderer
    lateinit var RENDERER_NGGL4ES: Renderer
    lateinit var RENDERER_MESA2520_EGL_NO_SURFACE: Renderer
    private var isInit = false

    @JvmStatic
    val rendererList: MutableList<Renderer> = mutableListOf()
        get() {
            if (!isInit) {
                init(FCLPath.CONTEXT)
            }
            return field
        }

    fun init(context: Context) {
        if (isInit) return
        isInit = true
        rendererList.clear()
        RENDERER_GL4ES = Renderer(
            "Holy-GL4ES",
            context.getString(R.string.settings_fcl_renderer_gl4es),
            "libgl4es_114.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_GL4ES,
            "",
            "1.21.4"
        )

        RENDERER_VIRGL = Renderer(
            "VirGLRenderer",
            context.getString(R.string.settings_fcl_renderer_virgl),
            "libOSMesa_81.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_VIRGL,
            "",
            ""
        )

        RENDERER_VGPU = Renderer(
            "VGPU",
            context.getString(R.string.settings_fcl_renderer_vgpu),
            "libvgpu.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_VGPU,
            "",
            "1.16.5"
        )

        RENDERER_ZINK = Renderer(
            "Zink",
            context.getString(R.string.settings_fcl_renderer_zink),
            "libOSMesa_8.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_ZINK,
            "",
            ""
        )

        RENDERER_FREEDRENO = Renderer(
            "Freedreno",
            context.getString(R.string.settings_fcl_renderer_freedreno),
            "libOSMesa_8.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_FREEDRENO,
            "",
            ""
        )

        RENDERER_NGGL4ES = Renderer(
            "Krypton Wrapper",
            context.getString(R.string.settings_fcl_renderer_nggl4es),
            "libng_gl4es.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_NGGL4ES,
            "",
            ""
        )

        RENDERER_MESA2520_EGL_NO_SURFACE = Renderer(
            "Mesa 25.2.0 EGL_NO_SURFACE",
            context.getString(R.string.splash_runtime_renderer_mesa2520),
            "libglxshim.so",
            "libEGL.so",
            FCLPath.MESA2520_PATH,
            listOf("DLOPEN=libgallium_dri.so,libEGL.so:LIBGL_ES=2:GALLIUM_DRIVER=zink:MESA_LOADER_DRIVER_OVERRIDE=zink:MESA_GL_VERSION_OVERRIDE=4.6:MESA_GLSL_VERSION_OVERRIDE=460:GALLIUM_THREAD=0"),
            listOf("DLOPEN=libgallium_dri.so,libEGL.so:LIBGL_ES=2:POJAV_RENDERER=opengles3_desktopgl_zink_egl_no_surface:GALLIUM_DRIVER=zink:MESA_LOADER_DRIVER_OVERRIDE=zink:MESA_GL_VERSION_OVERRIDE=4.6:MESA_GLSL_VERSION_OVERRIDE=460:GALLIUM_THREAD=0"),
            Renderer.ID_MESA_EGL_NO_SURFACE,
            "",
            ""
        )

        RendererPlugin.init(context)
        addRenderer()
        DriverPlugin.init(context)
    }

    private fun addRenderer() {
        rendererList.add(RENDERER_NGGL4ES)
        rendererList.add(RENDERER_GL4ES)
        rendererList.add(RENDERER_VIRGL)
        rendererList.add(RENDERER_VGPU)
        rendererList.add(RENDERER_ZINK)
        rendererList.add(RENDERER_FREEDRENO)
        rendererList.add(RENDERER_MESA2520_EGL_NO_SURFACE)
        rendererList.addAll(RendererPlugin.rendererList)
    }

    fun refresh(context: Context) {
        RendererPlugin.refresh(context)
        rendererList.clear()
        addRenderer()
    }

    @JvmStatic
    fun getRenderer(id: String): Renderer {
        return rendererList.find { it.id == id } ?: RENDERER_NGGL4ES
    }

}