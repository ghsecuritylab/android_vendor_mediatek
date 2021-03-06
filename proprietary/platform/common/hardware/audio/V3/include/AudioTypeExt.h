#ifndef __AUDIO_TYPE_EXT_H__
#define __AUDIO_TYPE_EXT_H__

#include "AudioType.h"

const unsigned int AUDIO_DEVICE_OUT_FM_TX = 0x100000;
const audio_mode_t AUDIO_MODE_IN_CALL_2 = (audio_mode_t)4;
const audio_mode_t AUDIO_MODE_IN_CALL_EXTERNAL = (audio_mode_t)5;
const audio_source_t AUDIO_SOURCE_VOICE_UNLOCK = (audio_source_t)80;
const audio_source_t AUDIO_SOURCE_CUSTOMIZATION1 = (audio_source_t)81;
const audio_source_t AUDIO_SOURCE_CUSTOMIZATION2 = (audio_source_t)82;
const audio_source_t AUDIO_SOURCE_CUSTOMIZATION3 = (audio_source_t)83;
const audio_source_t AUDIO_SOURCE_ANC = (audio_source_t)97;
const audio_source_t AUDIO_SOURCE_MATV = (audio_source_t)98;
const audio_source_t AUDIO_SOURCE_FM = (audio_source_t)99;
const audio_devices_t AUDIO_DEVICE_IN_FM = (audio_devices_t)0x81000000;
const audio_devices_t AUDIO_DEVICE_IN_SPK_FEED = (audio_devices_t)0x82000000;
const audio_devices_t AUDIO_DEVICE_IN_MATV = (audio_devices_t)0x88000000;

#endif
