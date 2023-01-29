/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * $Id$
 *
 * FCL Pbuffer.
 *
 * @author Tungsten
 * @version $Revision$
 */

#include <stdlib.h>
#include "org_lwjgl_opengl_FCLPbufferPeerInfo.h"
#include "org_lwjgl_opengl_Pbuffer.h"
#include "extgl.h"
#include "context.h"
#include "common_tools.h"

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_FCLDisplay_nGetPbufferCapabilities(JNIEnv *env, jclass clazz, jlong display) {
	EGLDisplay disp = (EGLDisplay)(intptr_t)display;
	if (!extgl_InitEGL(disp))
		return 0;
	// Only support the EGL 1.4 Pbuffers
	return org_lwjgl_opengl_Pbuffer_PBUFFER_SUPPORTED;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLPbufferPeerInfo_nInitHandle(JNIEnv *env, jclass clazz, jlong display, jobject peer_info_handle, jint width, jint height, jobject pixel_format) {
	EGLDisplay disp = (EGLDisplay)(intptr_t)display;
	if (!extgl_InitEGL(disp)) {
		throwException(env, "No Pbuffer support");
		return;
	}
	bool result = initPeerInfo(env, peer_info_handle, disp, pixel_format, false, EGL_PBUFFER_BIT);
	if (!result)
		return;
	const int buffer_attribs[] = {EGL_WIDTH, width,
				      EGL_HEIGHT, height,
				      EGL_LARGEST_PBUFFER, EGL_FALSE,
					  EGL_NONE, EGL_NONE};

	FCLPeerInfo *peer_info = (FCLPeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	EGLConfig *config = getFBConfigFromPeerInfo(env, peer_info);
	if (config != NULL) {
		EGLSurface buffer = lwjgl_eglCreatePbufferSurface(peer_info->display, *config, buffer_attribs);
		free(config);
		peer_info->drawable = buffer;
	}
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLPbufferPeerInfo_nDestroy
  (JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	FCLPeerInfo *peer_info = (FCLPeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	lwjgl_eglDestroySurface(peer_info->display, peer_info->drawable);
}