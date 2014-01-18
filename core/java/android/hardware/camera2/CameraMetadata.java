/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The base class for camera controls and information.
 *
 * <p>
 * This class defines the basic key/value map used for querying for camera
 * characteristics or capture results, and for setting camera request
 * parameters.
 * </p>
 *
 * <p>
 * All instances of CameraMetadata are immutable. The list of keys with {@link #getKeys()}
 * never changes, nor do the values returned by any key with {@link #get} throughout
 * the lifetime of the object.
 * </p>
 *
 * @see CameraDevice
 * @see CameraManager
 * @see CameraCharacteristics
 **/
public abstract class CameraMetadata {

    /**
     * Set a camera metadata field to a value. The field definitions can be
     * found in {@link CameraCharacteristics}, {@link CaptureResult}, and
     * {@link CaptureRequest}.
     *
     * @param key The metadata field to write.
     * @param value The value to set the field to, which must be of a matching
     * type to the key.
     *
     * @hide
     */
    protected CameraMetadata() {
    }

    /**
     * Get a camera metadata field value.
     *
     * <p>The field definitions can be
     * found in {@link CameraCharacteristics}, {@link CaptureResult}, and
     * {@link CaptureRequest}.</p>
     *
     * <p>Querying the value for the same key more than once will return a value
     * which is equal to the previous queried value.</p>
     *
     * @throws IllegalArgumentException if the key was not valid
     *
     * @param key The metadata field to read.
     * @return The value of that key, or {@code null} if the field is not set.
     */
    public abstract <T> T get(Key<T> key);

    /**
     * Returns a list of the keys contained in this map.
     *
     * <p>The list returned is not modifiable, so any attempts to modify it will throw
     * a {@code UnsupportedOperationException}.</p>
     *
     * <p>All values retrieved by a key from this list with {@link #get} are guaranteed to be
     * non-{@code null}. Each key is only listed once in the list. The order of the keys
     * is undefined.</p>
     *
     * @return List of the keys contained in this map.
     */
    public List<Key<?>> getKeys() {
        return Collections.unmodifiableList(getKeysStatic(this.getClass(), this));
    }

    /**
     * Return a list of all the Key<?> that are declared as a field inside of the class
     * {@code type}.
     *
     * <p>
     * Optionally, if {@code instance} is not null, then filter out any keys with null values.
     * </p>
     */
    /*package*/ static ArrayList<Key<?>> getKeysStatic(Class<? extends CameraMetadata> type,
            CameraMetadata instance) {
        ArrayList<Key<?>> keyList = new ArrayList<Key<?>>();

        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            // Filter for Keys that are public
            if (field.getType().isAssignableFrom(Key.class) &&
                    (field.getModifiers() & Modifier.PUBLIC) != 0) {
                Key<?> key;
                try {
                    key = (Key<?>) field.get(instance);
                } catch (IllegalAccessException e) {
                    throw new AssertionError("Can't get IllegalAccessException", e);
                } catch (IllegalArgumentException e) {
                    throw new AssertionError("Can't get IllegalArgumentException", e);
                }
                if (instance == null || instance.get(key) != null) {
                    keyList.add(key);
                }
            }
        }

        return keyList;
    }

    public static class Key<T> {

        private boolean mHasTag;
        private int mTag;
        private final Class<T> mType;
        private final String mName;

        /**
         * @hide
         */
        public Key(String name, Class<T> type) {
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            } else if (type == null) {
                throw new NullPointerException("Type needs to be non-null");
            }
            mName = name;
            mType = type;
        }

        public final String getName() {
            return mName;
        }

        @Override
        public final int hashCode() {
            return mName.hashCode();
        }

        @Override
        @SuppressWarnings("unchecked")
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Key)) {
                return false;
            }

            Key lhs = (Key) o;

            return mName.equals(lhs.mName) && mType.equals(lhs.mType);
        }

        /**
         * <p>
         * Get the tag corresponding to this key. This enables insertion into the
         * native metadata.
         * </p>
         *
         * <p>This value is looked up the first time, and cached subsequently.</p>
         *
         * @return The tag numeric value corresponding to the string
         *
         * @hide
         */
        public final int getTag() {
            if (!mHasTag) {
                mTag = CameraMetadataNative.getTag(mName);
                mHasTag = true;
            }
            return mTag;
        }

        /**
         * @hide
         */
        public final Class<T> getType() {
            return mType;
        }
    }

    /*@O~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~
     * The enum values below this point are generated from metadata
     * definitions in /system/media/camera/docs. Do not modify by hand or
     * modify the comment blocks at the start or end.
     *~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~*/

    //
    // Enumeration values for CameraCharacteristics#LENS_FACING
    //

    /**
     * @see CameraCharacteristics#LENS_FACING
     */
    public static final int LENS_FACING_FRONT = 0;

    /**
     * @see CameraCharacteristics#LENS_FACING
     */
    public static final int LENS_FACING_BACK = 1;

    //
    // Enumeration values for CameraCharacteristics#LED_AVAILABLE_LEDS
    //

    /**
     * <p>android.led.transmit control is used</p>
     * @see CameraCharacteristics#LED_AVAILABLE_LEDS
     * @hide
     */
    public static final int LED_AVAILABLE_LEDS_TRANSMIT = 0;

    //
    // Enumeration values for CameraCharacteristics#INFO_SUPPORTED_HARDWARE_LEVEL
    //

    /**
     * @see CameraCharacteristics#INFO_SUPPORTED_HARDWARE_LEVEL
     */
    public static final int INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED = 0;

    /**
     * @see CameraCharacteristics#INFO_SUPPORTED_HARDWARE_LEVEL
     */
    public static final int INFO_SUPPORTED_HARDWARE_LEVEL_FULL = 1;

    //
    // Enumeration values for CaptureRequest#COLOR_CORRECTION_MODE
    //

    /**
     * <p>Use the {@link CaptureRequest#COLOR_CORRECTION_TRANSFORM android.colorCorrection.transform} matrix
     * and {@link CaptureRequest#COLOR_CORRECTION_GAINS android.colorCorrection.gains} to do color conversion.</p>
     * <p>All advanced white balance adjustments (not specified
     * by our white balance pipeline) must be disabled.</p>
     * <p>If AWB is enabled with <code>{@link CaptureRequest#CONTROL_AWB_MODE android.control.awbMode} != OFF</code>, then
     * TRANSFORM_MATRIX is ignored. The camera device will override
     * this value to either FAST or HIGH_QUALITY.</p>
     *
     * @see CaptureRequest#COLOR_CORRECTION_GAINS
     * @see CaptureRequest#COLOR_CORRECTION_TRANSFORM
     * @see CaptureRequest#CONTROL_AWB_MODE
     * @see CaptureRequest#COLOR_CORRECTION_MODE
     */
    public static final int COLOR_CORRECTION_MODE_TRANSFORM_MATRIX = 0;

    /**
     * <p>Must not slow down capture rate relative to sensor raw
     * output.</p>
     * <p>Advanced white balance adjustments above and beyond
     * the specified white balance pipeline may be applied.</p>
     * <p>If AWB is enabled with <code>{@link CaptureRequest#CONTROL_AWB_MODE android.control.awbMode} != OFF</code>, then
     * the camera device uses the last frame's AWB values
     * (or defaults if AWB has never been run).</p>
     *
     * @see CaptureRequest#CONTROL_AWB_MODE
     * @see CaptureRequest#COLOR_CORRECTION_MODE
     */
    public static final int COLOR_CORRECTION_MODE_FAST = 1;

    /**
     * <p>Capture rate (relative to sensor raw output)
     * may be reduced by high quality.</p>
     * <p>Advanced white balance adjustments above and beyond
     * the specified white balance pipeline may be applied.</p>
     * <p>If AWB is enabled with <code>{@link CaptureRequest#CONTROL_AWB_MODE android.control.awbMode} != OFF</code>, then
     * the camera device uses the last frame's AWB values
     * (or defaults if AWB has never been run).</p>
     *
     * @see CaptureRequest#CONTROL_AWB_MODE
     * @see CaptureRequest#COLOR_CORRECTION_MODE
     */
    public static final int COLOR_CORRECTION_MODE_HIGH_QUALITY = 2;

    //
    // Enumeration values for CaptureRequest#CONTROL_AE_ANTIBANDING_MODE
    //

    /**
     * <p>The camera device will not adjust exposure duration to
     * avoid banding problems.</p>
     * @see CaptureRequest#CONTROL_AE_ANTIBANDING_MODE
     */
    public static final int CONTROL_AE_ANTIBANDING_MODE_OFF = 0;

    /**
     * <p>The camera device will adjust exposure duration to
     * avoid banding problems with 50Hz illumination sources.</p>
     * @see CaptureRequest#CONTROL_AE_ANTIBANDING_MODE
     */
    public static final int CONTROL_AE_ANTIBANDING_MODE_50HZ = 1;

    /**
     * <p>The camera device will adjust exposure duration to
     * avoid banding problems with 60Hz illumination
     * sources.</p>
     * @see CaptureRequest#CONTROL_AE_ANTIBANDING_MODE
     */
    public static final int CONTROL_AE_ANTIBANDING_MODE_60HZ = 2;

    /**
     * <p>The camera device will automatically adapt its
     * antibanding routine to the current illumination
     * conditions. This is the default.</p>
     * @see CaptureRequest#CONTROL_AE_ANTIBANDING_MODE
     */
    public static final int CONTROL_AE_ANTIBANDING_MODE_AUTO = 3;

    //
    // Enumeration values for CaptureRequest#CONTROL_AE_MODE
    //

    /**
     * <p>The camera device's autoexposure routine is disabled;
     * the application-selected {@link CaptureRequest#SENSOR_EXPOSURE_TIME android.sensor.exposureTime},
     * {@link CaptureRequest#SENSOR_SENSITIVITY android.sensor.sensitivity} and
     * {@link CaptureRequest#SENSOR_FRAME_DURATION android.sensor.frameDuration} are used by the camera
     * device, along with android.flash.* fields, if there's
     * a flash unit for this camera device.</p>
     *
     * @see CaptureRequest#SENSOR_EXPOSURE_TIME
     * @see CaptureRequest#SENSOR_FRAME_DURATION
     * @see CaptureRequest#SENSOR_SENSITIVITY
     * @see CaptureRequest#CONTROL_AE_MODE
     */
    public static final int CONTROL_AE_MODE_OFF = 0;

    /**
     * <p>The camera device's autoexposure routine is active,
     * with no flash control. The application's values for
     * {@link CaptureRequest#SENSOR_EXPOSURE_TIME android.sensor.exposureTime},
     * {@link CaptureRequest#SENSOR_SENSITIVITY android.sensor.sensitivity}, and
     * {@link CaptureRequest#SENSOR_FRAME_DURATION android.sensor.frameDuration} are ignored. The
     * application has control over the various
     * android.flash.* fields.</p>
     *
     * @see CaptureRequest#SENSOR_EXPOSURE_TIME
     * @see CaptureRequest#SENSOR_FRAME_DURATION
     * @see CaptureRequest#SENSOR_SENSITIVITY
     * @see CaptureRequest#CONTROL_AE_MODE
     */
    public static final int CONTROL_AE_MODE_ON = 1;

    /**
     * <p>Like ON, except that the camera device also controls
     * the camera's flash unit, firing it in low-light
     * conditions. The flash may be fired during a
     * precapture sequence (triggered by
     * {@link CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER android.control.aePrecaptureTrigger}) and may be fired
     * for captures for which the
     * {@link CaptureRequest#CONTROL_CAPTURE_INTENT android.control.captureIntent} field is set to
     * STILL_CAPTURE</p>
     *
     * @see CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     * @see CaptureRequest#CONTROL_AE_MODE
     */
    public static final int CONTROL_AE_MODE_ON_AUTO_FLASH = 2;

    /**
     * <p>Like ON, except that the camera device also controls
     * the camera's flash unit, always firing it for still
     * captures. The flash may be fired during a precapture
     * sequence (triggered by
     * {@link CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER android.control.aePrecaptureTrigger}) and will always
     * be fired for captures for which the
     * {@link CaptureRequest#CONTROL_CAPTURE_INTENT android.control.captureIntent} field is set to
     * STILL_CAPTURE</p>
     *
     * @see CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     * @see CaptureRequest#CONTROL_AE_MODE
     */
    public static final int CONTROL_AE_MODE_ON_ALWAYS_FLASH = 3;

    /**
     * <p>Like ON_AUTO_FLASH, but with automatic red eye
     * reduction. If deemed necessary by the camera device,
     * a red eye reduction flash will fire during the
     * precapture sequence.</p>
     * @see CaptureRequest#CONTROL_AE_MODE
     */
    public static final int CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE = 4;

    //
    // Enumeration values for CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER
    //

    /**
     * <p>The trigger is idle.</p>
     * @see CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER
     */
    public static final int CONTROL_AE_PRECAPTURE_TRIGGER_IDLE = 0;

    /**
     * <p>The precapture metering sequence will be started
     * by the camera device. The exact effect of the precapture
     * trigger depends on the current AE mode and state.</p>
     * @see CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER
     */
    public static final int CONTROL_AE_PRECAPTURE_TRIGGER_START = 1;

    //
    // Enumeration values for CaptureRequest#CONTROL_AF_MODE
    //

    /**
     * <p>The auto-focus routine does not control the lens;
     * {@link CaptureRequest#LENS_FOCUS_DISTANCE android.lens.focusDistance} is controlled by the
     * application</p>
     *
     * @see CaptureRequest#LENS_FOCUS_DISTANCE
     * @see CaptureRequest#CONTROL_AF_MODE
     */
    public static final int CONTROL_AF_MODE_OFF = 0;

    /**
     * <p>If lens is not fixed focus.</p>
     * <p>Use {@link CameraCharacteristics#LENS_INFO_MINIMUM_FOCUS_DISTANCE android.lens.info.minimumFocusDistance} to determine if lens
     * is fixed-focus. In this mode, the lens does not move unless
     * the autofocus trigger action is called. When that trigger
     * is activated, AF must transition to ACTIVE_SCAN, then to
     * the outcome of the scan (FOCUSED or NOT_FOCUSED).</p>
     * <p>Triggering AF_CANCEL resets the lens position to default,
     * and sets the AF state to INACTIVE.</p>
     *
     * @see CameraCharacteristics#LENS_INFO_MINIMUM_FOCUS_DISTANCE
     * @see CaptureRequest#CONTROL_AF_MODE
     */
    public static final int CONTROL_AF_MODE_AUTO = 1;

    /**
     * <p>In this mode, the lens does not move unless the
     * autofocus trigger action is called.</p>
     * <p>When that trigger is activated, AF must transition to
     * ACTIVE_SCAN, then to the outcome of the scan (FOCUSED or
     * NOT_FOCUSED).  Triggering cancel AF resets the lens
     * position to default, and sets the AF state to
     * INACTIVE.</p>
     * @see CaptureRequest#CONTROL_AF_MODE
     */
    public static final int CONTROL_AF_MODE_MACRO = 2;

    /**
     * <p>In this mode, the AF algorithm modifies the lens
     * position continually to attempt to provide a
     * constantly-in-focus image stream.</p>
     * <p>The focusing behavior should be suitable for good quality
     * video recording; typically this means slower focus
     * movement and no overshoots. When the AF trigger is not
     * involved, the AF algorithm should start in INACTIVE state,
     * and then transition into PASSIVE_SCAN and PASSIVE_FOCUSED
     * states as appropriate. When the AF trigger is activated,
     * the algorithm should immediately transition into
     * AF_FOCUSED or AF_NOT_FOCUSED as appropriate, and lock the
     * lens position until a cancel AF trigger is received.</p>
     * <p>Once cancel is received, the algorithm should transition
     * back to INACTIVE and resume passive scan. Note that this
     * behavior is not identical to CONTINUOUS_PICTURE, since an
     * ongoing PASSIVE_SCAN must immediately be
     * canceled.</p>
     * @see CaptureRequest#CONTROL_AF_MODE
     */
    public static final int CONTROL_AF_MODE_CONTINUOUS_VIDEO = 3;

    /**
     * <p>In this mode, the AF algorithm modifies the lens
     * position continually to attempt to provide a
     * constantly-in-focus image stream.</p>
     * <p>The focusing behavior should be suitable for still image
     * capture; typically this means focusing as fast as
     * possible. When the AF trigger is not involved, the AF
     * algorithm should start in INACTIVE state, and then
     * transition into PASSIVE_SCAN and PASSIVE_FOCUSED states as
     * appropriate as it attempts to maintain focus. When the AF
     * trigger is activated, the algorithm should finish its
     * PASSIVE_SCAN if active, and then transition into
     * AF_FOCUSED or AF_NOT_FOCUSED as appropriate, and lock the
     * lens position until a cancel AF trigger is received.</p>
     * <p>When the AF cancel trigger is activated, the algorithm
     * should transition back to INACTIVE and then act as if it
     * has just been started.</p>
     * @see CaptureRequest#CONTROL_AF_MODE
     */
    public static final int CONTROL_AF_MODE_CONTINUOUS_PICTURE = 4;

    /**
     * <p>Extended depth of field (digital focus). AF
     * trigger is ignored, AF state should always be
     * INACTIVE.</p>
     * @see CaptureRequest#CONTROL_AF_MODE
     */
    public static final int CONTROL_AF_MODE_EDOF = 5;

    //
    // Enumeration values for CaptureRequest#CONTROL_AF_TRIGGER
    //

    /**
     * <p>The trigger is idle.</p>
     * @see CaptureRequest#CONTROL_AF_TRIGGER
     */
    public static final int CONTROL_AF_TRIGGER_IDLE = 0;

    /**
     * <p>Autofocus will trigger now.</p>
     * @see CaptureRequest#CONTROL_AF_TRIGGER
     */
    public static final int CONTROL_AF_TRIGGER_START = 1;

    /**
     * <p>Autofocus will return to its initial
     * state, and cancel any currently active trigger.</p>
     * @see CaptureRequest#CONTROL_AF_TRIGGER
     */
    public static final int CONTROL_AF_TRIGGER_CANCEL = 2;

    //
    // Enumeration values for CaptureRequest#CONTROL_AWB_MODE
    //

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the application-selected color transform matrix
     * ({@link CaptureRequest#COLOR_CORRECTION_TRANSFORM android.colorCorrection.transform}) and gains
     * ({@link CaptureRequest#COLOR_CORRECTION_GAINS android.colorCorrection.gains}) are used by the camera
     * device for manual white balance control.</p>
     *
     * @see CaptureRequest#COLOR_CORRECTION_GAINS
     * @see CaptureRequest#COLOR_CORRECTION_TRANSFORM
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_OFF = 0;

    /**
     * <p>The camera device's auto white balance routine is active;
     * the application's values for android.colorCorrection.transform
     * and {@link CaptureRequest#COLOR_CORRECTION_GAINS android.colorCorrection.gains} are ignored.</p>
     *
     * @see CaptureRequest#COLOR_CORRECTION_GAINS
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_AUTO = 1;

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the camera device uses incandescent light as the assumed scene
     * illumination for white balance. While the exact white balance
     * transforms are up to the camera device, they will approximately
     * match the CIE standard illuminant A.</p>
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_INCANDESCENT = 2;

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the camera device uses fluorescent light as the assumed scene
     * illumination for white balance. While the exact white balance
     * transforms are up to the camera device, they will approximately
     * match the CIE standard illuminant F2.</p>
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_FLUORESCENT = 3;

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the camera device uses warm fluorescent light as the assumed scene
     * illumination for white balance. While the exact white balance
     * transforms are up to the camera device, they will approximately
     * match the CIE standard illuminant F4.</p>
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_WARM_FLUORESCENT = 4;

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the camera device uses daylight light as the assumed scene
     * illumination for white balance. While the exact white balance
     * transforms are up to the camera device, they will approximately
     * match the CIE standard illuminant D65.</p>
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_DAYLIGHT = 5;

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the camera device uses cloudy daylight light as the assumed scene
     * illumination for white balance.</p>
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_CLOUDY_DAYLIGHT = 6;

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the camera device uses twilight light as the assumed scene
     * illumination for white balance.</p>
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_TWILIGHT = 7;

    /**
     * <p>The camera device's auto white balance routine is disabled;
     * the camera device uses shade light as the assumed scene
     * illumination for white balance.</p>
     * @see CaptureRequest#CONTROL_AWB_MODE
     */
    public static final int CONTROL_AWB_MODE_SHADE = 8;

    //
    // Enumeration values for CaptureRequest#CONTROL_CAPTURE_INTENT
    //

    /**
     * <p>This request doesn't fall into the other
     * categories. Default to preview-like
     * behavior.</p>
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     */
    public static final int CONTROL_CAPTURE_INTENT_CUSTOM = 0;

    /**
     * <p>This request is for a preview-like usecase. The
     * precapture trigger may be used to start off a metering
     * w/flash sequence</p>
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     */
    public static final int CONTROL_CAPTURE_INTENT_PREVIEW = 1;

    /**
     * <p>This request is for a still capture-type
     * usecase.</p>
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     */
    public static final int CONTROL_CAPTURE_INTENT_STILL_CAPTURE = 2;

    /**
     * <p>This request is for a video recording
     * usecase.</p>
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     */
    public static final int CONTROL_CAPTURE_INTENT_VIDEO_RECORD = 3;

    /**
     * <p>This request is for a video snapshot (still
     * image while recording video) usecase</p>
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     */
    public static final int CONTROL_CAPTURE_INTENT_VIDEO_SNAPSHOT = 4;

    /**
     * <p>This request is for a ZSL usecase; the
     * application will stream full-resolution images and
     * reprocess one or several later for a final
     * capture</p>
     * @see CaptureRequest#CONTROL_CAPTURE_INTENT
     */
    public static final int CONTROL_CAPTURE_INTENT_ZERO_SHUTTER_LAG = 5;

    //
    // Enumeration values for CaptureRequest#CONTROL_EFFECT_MODE
    //

    /**
     * <p>No color effect will be applied.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_OFF = 0;

    /**
     * <p>A "monocolor" effect where the image is mapped into
     * a single color.  This will typically be grayscale.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_MONO = 1;

    /**
     * <p>A "photo-negative" effect where the image's colors
     * are inverted.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_NEGATIVE = 2;

    /**
     * <p>A "solarisation" effect (Sabattier effect) where the
     * image is wholly or partially reversed in
     * tone.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_SOLARIZE = 3;

    /**
     * <p>A "sepia" effect where the image is mapped into warm
     * gray, red, and brown tones.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_SEPIA = 4;

    /**
     * <p>A "posterization" effect where the image uses
     * discrete regions of tone rather than a continuous
     * gradient of tones.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_POSTERIZE = 5;

    /**
     * <p>A "whiteboard" effect where the image is typically displayed
     * as regions of white, with black or grey details.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_WHITEBOARD = 6;

    /**
     * <p>A "blackboard" effect where the image is typically displayed
     * as regions of black, with white or grey details.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_BLACKBOARD = 7;

    /**
     * <p>An "aqua" effect where a blue hue is added to the image.</p>
     * @see CaptureRequest#CONTROL_EFFECT_MODE
     */
    public static final int CONTROL_EFFECT_MODE_AQUA = 8;

    //
    // Enumeration values for CaptureRequest#CONTROL_MODE
    //

    /**
     * <p>Full application control of pipeline. All 3A
     * routines are disabled, no other settings in
     * android.control.* have any effect</p>
     * @see CaptureRequest#CONTROL_MODE
     */
    public static final int CONTROL_MODE_OFF = 0;

    /**
     * <p>Use settings for each individual 3A routine.
     * Manual control of capture parameters is disabled. All
     * controls in android.control.* besides sceneMode take
     * effect</p>
     * @see CaptureRequest#CONTROL_MODE
     */
    public static final int CONTROL_MODE_AUTO = 1;

    /**
     * <p>Use specific scene mode. Enabling this disables
     * control.aeMode, control.awbMode and control.afMode
     * controls; the HAL must ignore those settings while
     * USE_SCENE_MODE is active (except for FACE_PRIORITY
     * scene mode). Other control entries are still active.
     * This setting can only be used if availableSceneModes !=
     * UNSUPPORTED</p>
     * @see CaptureRequest#CONTROL_MODE
     */
    public static final int CONTROL_MODE_USE_SCENE_MODE = 2;

    //
    // Enumeration values for CaptureRequest#CONTROL_SCENE_MODE
    //

    /**
     * <p>Indicates that no scene modes are set for a given capture request.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_DISABLED = 0;

    /**
     * <p>If face detection support exists, use face
     * detection data for auto-focus, auto-white balance, and
     * auto-exposure routines. If face detection statistics are
     * disabled (i.e. {@link CaptureRequest#STATISTICS_FACE_DETECT_MODE android.statistics.faceDetectMode} is set to OFF),
     * this should still operate correctly (but will not return
     * face detection statistics to the framework).</p>
     * <p>Unlike the other scene modes, {@link CaptureRequest#CONTROL_AE_MODE android.control.aeMode},
     * {@link CaptureRequest#CONTROL_AWB_MODE android.control.awbMode}, and android.control.afMode
     * remain active when FACE_PRIORITY is set.</p>
     *
     * @see CaptureRequest#CONTROL_AE_MODE
     * @see CaptureRequest#CONTROL_AWB_MODE
     * @see CaptureRequest#STATISTICS_FACE_DETECT_MODE
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_FACE_PRIORITY = 1;

    /**
     * <p>Optimized for photos of quickly moving objects.
     * Similar to SPORTS.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_ACTION = 2;

    /**
     * <p>Optimized for still photos of people.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_PORTRAIT = 3;

    /**
     * <p>Optimized for photos of distant macroscopic objects.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_LANDSCAPE = 4;

    /**
     * <p>Optimized for low-light settings.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_NIGHT = 5;

    /**
     * <p>Optimized for still photos of people in low-light
     * settings.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_NIGHT_PORTRAIT = 6;

    /**
     * <p>Optimized for dim, indoor settings where flash must
     * remain off.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_THEATRE = 7;

    /**
     * <p>Optimized for bright, outdoor beach settings.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_BEACH = 8;

    /**
     * <p>Optimized for bright, outdoor settings containing snow.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_SNOW = 9;

    /**
     * <p>Optimized for scenes of the setting sun.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_SUNSET = 10;

    /**
     * <p>Optimized to avoid blurry photos due to small amounts of
     * device motion (for example: due to hand shake).</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_STEADYPHOTO = 11;

    /**
     * <p>Optimized for nighttime photos of fireworks.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_FIREWORKS = 12;

    /**
     * <p>Optimized for photos of quickly moving people.
     * Similar to ACTION.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_SPORTS = 13;

    /**
     * <p>Optimized for dim, indoor settings with multiple moving
     * people.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_PARTY = 14;

    /**
     * <p>Optimized for dim settings where the main light source
     * is a flame.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_CANDLELIGHT = 15;

    /**
     * <p>Optimized for accurately capturing a photo of barcode
     * for use by camera applications that wish to read the
     * barcode value.</p>
     * @see CaptureRequest#CONTROL_SCENE_MODE
     */
    public static final int CONTROL_SCENE_MODE_BARCODE = 16;

    //
    // Enumeration values for CaptureRequest#EDGE_MODE
    //

    /**
     * <p>No edge enhancement is applied</p>
     * @see CaptureRequest#EDGE_MODE
     */
    public static final int EDGE_MODE_OFF = 0;

    /**
     * <p>Must not slow down frame rate relative to sensor
     * output</p>
     * @see CaptureRequest#EDGE_MODE
     */
    public static final int EDGE_MODE_FAST = 1;

    /**
     * <p>Frame rate may be reduced by high
     * quality</p>
     * @see CaptureRequest#EDGE_MODE
     */
    public static final int EDGE_MODE_HIGH_QUALITY = 2;

    //
    // Enumeration values for CaptureRequest#FLASH_MODE
    //

    /**
     * <p>Do not fire the flash for this capture.</p>
     * @see CaptureRequest#FLASH_MODE
     */
    public static final int FLASH_MODE_OFF = 0;

    /**
     * <p>If the flash is available and charged, fire flash
     * for this capture based on android.flash.firingPower and
     * android.flash.firingTime.</p>
     * @see CaptureRequest#FLASH_MODE
     */
    public static final int FLASH_MODE_SINGLE = 1;

    /**
     * <p>Transition flash to continuously on.</p>
     * @see CaptureRequest#FLASH_MODE
     */
    public static final int FLASH_MODE_TORCH = 2;

    //
    // Enumeration values for CaptureRequest#LENS_OPTICAL_STABILIZATION_MODE
    //

    /**
     * @see CaptureRequest#LENS_OPTICAL_STABILIZATION_MODE
     */
    public static final int LENS_OPTICAL_STABILIZATION_MODE_OFF = 0;

    /**
     * @see CaptureRequest#LENS_OPTICAL_STABILIZATION_MODE
     */
    public static final int LENS_OPTICAL_STABILIZATION_MODE_ON = 1;

    //
    // Enumeration values for CaptureRequest#NOISE_REDUCTION_MODE
    //

    /**
     * <p>No noise reduction is applied</p>
     * @see CaptureRequest#NOISE_REDUCTION_MODE
     */
    public static final int NOISE_REDUCTION_MODE_OFF = 0;

    /**
     * <p>Must not slow down frame rate relative to sensor
     * output</p>
     * @see CaptureRequest#NOISE_REDUCTION_MODE
     */
    public static final int NOISE_REDUCTION_MODE_FAST = 1;

    /**
     * <p>May slow down frame rate to provide highest
     * quality</p>
     * @see CaptureRequest#NOISE_REDUCTION_MODE
     */
    public static final int NOISE_REDUCTION_MODE_HIGH_QUALITY = 2;

    //
    // Enumeration values for CaptureRequest#SHADING_MODE
    //

    /**
     * <p>No lens shading correction is applied</p>
     * @see CaptureRequest#SHADING_MODE
     * @hide
     */
    public static final int SHADING_MODE_OFF = 0;

    /**
     * <p>Must not slow down frame rate relative to sensor raw output</p>
     * @see CaptureRequest#SHADING_MODE
     * @hide
     */
    public static final int SHADING_MODE_FAST = 1;

    /**
     * <p>Frame rate may be reduced by high quality</p>
     * @see CaptureRequest#SHADING_MODE
     * @hide
     */
    public static final int SHADING_MODE_HIGH_QUALITY = 2;

    //
    // Enumeration values for CaptureRequest#STATISTICS_FACE_DETECT_MODE
    //

    /**
     * @see CaptureRequest#STATISTICS_FACE_DETECT_MODE
     */
    public static final int STATISTICS_FACE_DETECT_MODE_OFF = 0;

    /**
     * <p>Optional Return rectangle and confidence
     * only</p>
     * @see CaptureRequest#STATISTICS_FACE_DETECT_MODE
     */
    public static final int STATISTICS_FACE_DETECT_MODE_SIMPLE = 1;

    /**
     * <p>Optional Return all face
     * metadata</p>
     * @see CaptureRequest#STATISTICS_FACE_DETECT_MODE
     */
    public static final int STATISTICS_FACE_DETECT_MODE_FULL = 2;

    //
    // Enumeration values for CaptureRequest#STATISTICS_LENS_SHADING_MAP_MODE
    //

    /**
     * @see CaptureRequest#STATISTICS_LENS_SHADING_MAP_MODE
     */
    public static final int STATISTICS_LENS_SHADING_MAP_MODE_OFF = 0;

    /**
     * @see CaptureRequest#STATISTICS_LENS_SHADING_MAP_MODE
     */
    public static final int STATISTICS_LENS_SHADING_MAP_MODE_ON = 1;

    //
    // Enumeration values for CaptureRequest#TONEMAP_MODE
    //

    /**
     * <p>Use the tone mapping curve specified in
     * android.tonemap.curve</p>
     * @see CaptureRequest#TONEMAP_MODE
     */
    public static final int TONEMAP_MODE_CONTRAST_CURVE = 0;

    /**
     * <p>Must not slow down frame rate relative to raw
     * bayer output</p>
     * @see CaptureRequest#TONEMAP_MODE
     */
    public static final int TONEMAP_MODE_FAST = 1;

    /**
     * <p>Frame rate may be reduced by high
     * quality</p>
     * @see CaptureRequest#TONEMAP_MODE
     */
    public static final int TONEMAP_MODE_HIGH_QUALITY = 2;

    //
    // Enumeration values for CaptureResult#CONTROL_AE_STATE
    //

    /**
     * <p>AE is off or recently reset. When a camera device is opened, it starts in
     * this state.</p>
     * @see CaptureResult#CONTROL_AE_STATE
     */
    public static final int CONTROL_AE_STATE_INACTIVE = 0;

    /**
     * <p>AE doesn't yet have a good set of control values
     * for the current scene.</p>
     * @see CaptureResult#CONTROL_AE_STATE
     */
    public static final int CONTROL_AE_STATE_SEARCHING = 1;

    /**
     * <p>AE has a good set of control values for the
     * current scene.</p>
     * @see CaptureResult#CONTROL_AE_STATE
     */
    public static final int CONTROL_AE_STATE_CONVERGED = 2;

    /**
     * <p>AE has been locked.</p>
     * @see CaptureResult#CONTROL_AE_STATE
     */
    public static final int CONTROL_AE_STATE_LOCKED = 3;

    /**
     * <p>AE has a good set of control values, but flash
     * needs to be fired for good quality still
     * capture.</p>
     * @see CaptureResult#CONTROL_AE_STATE
     */
    public static final int CONTROL_AE_STATE_FLASH_REQUIRED = 4;

    /**
     * <p>AE has been asked to do a precapture sequence
     * (through the {@link CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER android.control.aePrecaptureTrigger} START),
     * and is currently executing it. Once PRECAPTURE
     * completes, AE will transition to CONVERGED or
     * FLASH_REQUIRED as appropriate.</p>
     *
     * @see CaptureRequest#CONTROL_AE_PRECAPTURE_TRIGGER
     * @see CaptureResult#CONTROL_AE_STATE
     */
    public static final int CONTROL_AE_STATE_PRECAPTURE = 5;

    //
    // Enumeration values for CaptureResult#CONTROL_AF_STATE
    //

    /**
     * <p>AF off or has not yet tried to scan/been asked
     * to scan.  When a camera device is opened, it starts in
     * this state.</p>
     * @see CaptureResult#CONTROL_AF_STATE
     */
    public static final int CONTROL_AF_STATE_INACTIVE = 0;

    /**
     * <p>if CONTINUOUS_* modes are supported. AF is
     * currently doing an AF scan initiated by a continuous
     * autofocus mode</p>
     * @see CaptureResult#CONTROL_AF_STATE
     */
    public static final int CONTROL_AF_STATE_PASSIVE_SCAN = 1;

    /**
     * <p>if CONTINUOUS_* modes are supported. AF currently
     * believes it is in focus, but may restart scanning at
     * any time.</p>
     * @see CaptureResult#CONTROL_AF_STATE
     */
    public static final int CONTROL_AF_STATE_PASSIVE_FOCUSED = 2;

    /**
     * <p>if AUTO or MACRO modes are supported. AF is doing
     * an AF scan because it was triggered by AF
     * trigger</p>
     * @see CaptureResult#CONTROL_AF_STATE
     */
    public static final int CONTROL_AF_STATE_ACTIVE_SCAN = 3;

    /**
     * <p>if any AF mode besides OFF is supported. AF
     * believes it is focused correctly and is
     * locked</p>
     * @see CaptureResult#CONTROL_AF_STATE
     */
    public static final int CONTROL_AF_STATE_FOCUSED_LOCKED = 4;

    /**
     * <p>if any AF mode besides OFF is supported. AF has
     * failed to focus successfully and is
     * locked</p>
     * @see CaptureResult#CONTROL_AF_STATE
     */
    public static final int CONTROL_AF_STATE_NOT_FOCUSED_LOCKED = 5;

    /**
     * <p>if CONTINUOUS_* modes are supported. AF finished a
     * passive scan without finding focus, and may restart
     * scanning at any time.</p>
     * @see CaptureResult#CONTROL_AF_STATE
     */
    public static final int CONTROL_AF_STATE_PASSIVE_UNFOCUSED = 6;

    //
    // Enumeration values for CaptureResult#CONTROL_AWB_STATE
    //

    /**
     * <p>AWB is not in auto mode.  When a camera device is opened, it
     * starts in this state.</p>
     * @see CaptureResult#CONTROL_AWB_STATE
     */
    public static final int CONTROL_AWB_STATE_INACTIVE = 0;

    /**
     * <p>AWB doesn't yet have a good set of control
     * values for the current scene.</p>
     * @see CaptureResult#CONTROL_AWB_STATE
     */
    public static final int CONTROL_AWB_STATE_SEARCHING = 1;

    /**
     * <p>AWB has a good set of control values for the
     * current scene.</p>
     * @see CaptureResult#CONTROL_AWB_STATE
     */
    public static final int CONTROL_AWB_STATE_CONVERGED = 2;

    /**
     * <p>AWB has been locked.</p>
     * @see CaptureResult#CONTROL_AWB_STATE
     */
    public static final int CONTROL_AWB_STATE_LOCKED = 3;

    //
    // Enumeration values for CaptureResult#FLASH_STATE
    //

    /**
     * <p>No flash on camera</p>
     * @see CaptureResult#FLASH_STATE
     */
    public static final int FLASH_STATE_UNAVAILABLE = 0;

    /**
     * <p>if android.flash.available is true Flash is
     * charging and cannot be fired</p>
     * @see CaptureResult#FLASH_STATE
     */
    public static final int FLASH_STATE_CHARGING = 1;

    /**
     * <p>if android.flash.available is true Flash is
     * ready to fire</p>
     * @see CaptureResult#FLASH_STATE
     */
    public static final int FLASH_STATE_READY = 2;

    /**
     * <p>if android.flash.available is true Flash fired
     * for this capture</p>
     * @see CaptureResult#FLASH_STATE
     */
    public static final int FLASH_STATE_FIRED = 3;

    //
    // Enumeration values for CaptureResult#LENS_STATE
    //

    /**
     * @see CaptureResult#LENS_STATE
     */
    public static final int LENS_STATE_STATIONARY = 0;

    /**
     * @see CaptureResult#LENS_STATE
     */
    public static final int LENS_STATE_MOVING = 1;

    //
    // Enumeration values for CaptureResult#STATISTICS_SCENE_FLICKER
    //

    /**
     * @see CaptureResult#STATISTICS_SCENE_FLICKER
     */
    public static final int STATISTICS_SCENE_FLICKER_NONE = 0;

    /**
     * @see CaptureResult#STATISTICS_SCENE_FLICKER
     */
    public static final int STATISTICS_SCENE_FLICKER_50HZ = 1;

    /**
     * @see CaptureResult#STATISTICS_SCENE_FLICKER
     */
    public static final int STATISTICS_SCENE_FLICKER_60HZ = 2;

    /*~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~
     * End generated code
     *~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~@~O@*/

}
