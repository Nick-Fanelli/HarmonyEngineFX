package com.harmony.engine.core.io;

import com.harmony.engine.core.Harmony;
import com.harmony.engine.math.Vector2f;

import java.awt.event.*;

/**
 * The type Input.
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    /* Key Codes. */

    /**
     * Constant for the ENTER virtual key.
     */
    public static final int ENTER          = '\n';

    /**
     * Constant for the BACK_SPACE virtual key.
     */
    public static final int BACK_SPACE     = '\b';

    /**
     * Constant for the TAB virtual key.
     */
    public static final int TAB            = '\t';

    /**
     * Constant for the CANCEL virtual key.
     */
    public static final int CANCEL         = 0x03;

    /**
     * Constant for the CLEAR virtual key.
     */
    public static final int CLEAR          = 0x0C;

    /**
     * Constant for the SHIFT virtual key.
     */
    public static final int SHIFT          = 0x10;

    /**
     * Constant for the CONTROL virtual key.
     */
    public static final int CONTROL        = 0x11;

    /**
     * Constant for the ALT virtual key.
     */
    public static final int ALT            = 0x12;

    /**
     * Constant for the PAUSE virtual key.
     */
    public static final int PAUSE          = 0x13;

    /**
     * Constant for the CAPS_LOCK virtual key.
     */
    public static final int CAPS_LOCK      = 0x14;

    /**
     * Constant for the ESCAPE virtual key.
     */
    public static final int ESCAPE         = 0x1B;

    /**
     * Constant for the SPACE virtual key.
     */
    public static final int SPACE          = 0x20;

    /**
     * Constant for the PAGE_UP virtual key.
     */
    public static final int PAGE_UP        = 0x21;

    /**
     * Constant for the PAGE_DOWN virtual key.
     */
    public static final int PAGE_DOWN      = 0x22;

    /**
     * Constant for the END virtual key.
     */
    public static final int END            = 0x23;

    /**
     * Constant for the HOME virtual key.
     */
    public static final int HOME           = 0x24;

    /**
     * Constant for the non-numpad <b>left</b> arrow key.
     *
     * @see #KP_LEFT #KP_LEFT
     */
    public static final int LEFT           = 0x25;

    /**
     * Constant for the non-numpad <b>up</b> arrow key.
     *
     * @see #KP_UP #KP_UP
     */
    public static final int UP             = 0x26;

    /**
     * Constant for the non-numpad <b>right</b> arrow key.
     *
     * @see #KP_RIGHT #KP_RIGHT
     */
    public static final int RIGHT          = 0x27;

    /**
     * Constant for the non-numpad <b>down</b> arrow key.
     *
     * @see #KP_DOWN #KP_DOWN
     */
    public static final int DOWN           = 0x28;

    /**
     * Constant for the comma key, ","
     */
    public static final int COMMA          = 0x2C;

    /**
     * Constant for the minus key, "-"
     *
     * @since 1.2
     */
    public static final int MINUS          = 0x2D;

    /**
     * Constant for the period key, "."
     */
    public static final int PERIOD         = 0x2E;

    /**
     * Constant for the forward slash key, "/"
     */
    public static final int SLASH          = 0x2F;

    /** 0 thru 9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */

    /**
     * Constant for the "0" key.
     */
    public static final int NUMBER_0              = 0x30;

    /**
     * Constant for the "1" key.
     */
    public static final int NUMBER_1              = 0x31;

    /**
     * Constant for the "2" key.
     */
    public static final int NUMBER_2              = 0x32;

    /**
     * Constant for the "3" key.
     */
    public static final int NUMBER_3              = 0x33;

    /**
     * Constant for the "4" key.
     */
    public static final int NUMBER_4              = 0x34;

    /**
     * Constant for the "5" key.
     */
    public static final int NUMBER_5              = 0x35;

    /**
     * Constant for the "6" key.
     */
    public static final int NUMBER_6              = 0x36;

    /**
     * Constant for the "7" key.
     */
    public static final int NUMBER_7              = 0x37;

    /**
     * Constant for the "8" key.
     */
    public static final int NUMBER_8              = 0x38;

    /**
     * Constant for the "9" key.
     */
    public static final int NUMBER_9              = 0x39;

    /**
     * Constant for the semicolon key, ";"
     */
    public static final int SEMICOLON      = 0x3B;

    /**
     * Constant for the equals key, "="
     */
    public static final int EQUALS         = 0x3D;

    /** A thru Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */

    /**
     * Constant for the "A" key.
     */
    public static final int A              = 0x41;

    /**
     * Constant for the "B" key.
     */
    public static final int B              = 0x42;

    /**
     * Constant for the "C" key.
     */
    public static final int C              = 0x43;

    /**
     * Constant for the "D" key.
     */
    public static final int D              = 0x44;

    /**
     * Constant for the "E" key.
     */
    public static final int E              = 0x45;

    /**
     * Constant for the "F" key.
     */
    public static final int F              = 0x46;

    /**
     * Constant for the "G" key.
     */
    public static final int G              = 0x47;

    /**
     * Constant for the "H" key.
     */
    public static final int H              = 0x48;

    /**
     * Constant for the "I" key.
     */
    public static final int I              = 0x49;

    /**
     * Constant for the "J" key.
     */
    public static final int J              = 0x4A;

    /**
     * Constant for the "K" key.
     */
    public static final int K              = 0x4B;

    /**
     * Constant for the "L" key.
     */
    public static final int L              = 0x4C;

    /**
     * Constant for the "M" key.
     */
    public static final int M              = 0x4D;

    /**
     * Constant for the "N" key.
     */
    public static final int N              = 0x4E;

    /**
     * Constant for the "O" key.
     */
    public static final int O              = 0x4F;

    /**
     * Constant for the "P" key.
     */
    public static final int P              = 0x50;

    /**
     * Constant for the "Q" key.
     */
    public static final int Q              = 0x51;

    /**
     * Constant for the "R" key.
     */
    public static final int R              = 0x52;

    /**
     * Constant for the "S" key.
     */
    public static final int S              = 0x53;

    /**
     * Constant for the "T" key.
     */
    public static final int T              = 0x54;

    /**
     * Constant for the "U" key.
     */
    public static final int U              = 0x55;

    /**
     * Constant for the "V" key.
     */
    public static final int V              = 0x56;

    /**
     * Constant for the "W" key.
     */
    public static final int W              = 0x57;

    /**
     * Constant for the "X" key.
     */
    public static final int X              = 0x58;

    /**
     * Constant for the "Y" key.
     */
    public static final int Y              = 0x59;

    /**
     * Constant for the "Z" key.
     */
    public static final int Z              = 0x5A;

    /**
     * Constant for the open bracket key, "["
     */
    public static final int OPEN_BRACKET   = 0x5B;

    /**
     * Constant for the back slash key, "\"
     */
    public static final int BACK_SLASH     = 0x5C;

    /**
     * Constant for the close bracket key, "]"
     */
    public static final int CLOSE_BRACKET  = 0x5D;

    /**
     * Constant for the number pad "0" key.
     */
    public static final int NUMPAD0        = 0x60;

    /**
     * Constant for the number pad "1" key.
     */
    public static final int NUMPAD1        = 0x61;

    /**
     * Constant for the number pad "2" key.
     */
    public static final int NUMPAD2        = 0x62;

    /**
     * Constant for the number pad "3" key.
     */
    public static final int NUMPAD3        = 0x63;

    /**
     * Constant for the number pad "4" key.
     */
    public static final int NUMPAD4        = 0x64;

    /**
     * Constant for the number pad "5" key.
     */
    public static final int NUMPAD5        = 0x65;

    /**
     * Constant for the number pad "6" key.
     */
    public static final int NUMPAD6        = 0x66;

    /**
     * Constant for the number pad "7" key.
     */
    public static final int NUMPAD7        = 0x67;

    /**
     * Constant for the number pad "8" key.
     */
    public static final int NUMPAD8        = 0x68;

    /**
     * Constant for the number pad "9" key.
     */
    public static final int NUMPAD9        = 0x69;

    /**
     * Constant for the number pad multiply key.
     */
    public static final int MULTIPLY       = 0x6A;

    /**
     * Constant for the number pad add key.
     */
    public static final int ADD            = 0x6B;

    /**
     * This constant is obsolete, and is included only for backwards
     * compatibility.
     *
     * @see #SEPARATOR #SEPARATOR
     */
    public static final int SEPARATER      = 0x6C;

    /**
     * Constant for the Numpad Separator key.
     *
     * @since 1.4
     */
    public static final int SEPARATOR      = SEPARATER;

    /**
     * Constant for the number pad subtract key.
     */
    public static final int SUBTRACT       = 0x6D;

    /**
     * Constant for the number pad decimal point key.
     */
    public static final int DECIMAL        = 0x6E;

    /**
     * Constant for the number pad divide key.
     */
    public static final int DIVIDE         = 0x6F;

    /**
     * Constant for the delete key.
     */
    public static final int DELETE         = 0x7F; /* ASCII DEL */

    /**
     * Constant for the NUM_LOCK key.
     */
    public static final int NUM_LOCK       = 0x90;

    /**
     * Constant for the SCROLL_LOCK key.
     */
    public static final int SCROLL_LOCK    = 0x91;

    /**
     * Constant for the F1 function key.
     */
    public static final int F1             = 0x70;

    /**
     * Constant for the F2 function key.
     */
    public static final int F2             = 0x71;

    /**
     * Constant for the F3 function key.
     */
    public static final int F3             = 0x72;

    /**
     * Constant for the F4 function key.
     */
    public static final int F4             = 0x73;

    /**
     * Constant for the F5 function key.
     */
    public static final int F5             = 0x74;

    /**
     * Constant for the F6 function key.
     */
    public static final int F6             = 0x75;

    /**
     * Constant for the F7 function key.
     */
    public static final int F7             = 0x76;

    /**
     * Constant for the F8 function key.
     */
    public static final int F8             = 0x77;

    /**
     * Constant for the F9 function key.
     */
    public static final int F9             = 0x78;

    /**
     * Constant for the F10 function key.
     */
    public static final int F10            = 0x79;

    /**
     * Constant for the F11 function key.
     */
    public static final int F11            = 0x7A;

    /**
     * Constant for the F12 function key.
     */
    public static final int F12            = 0x7B;

    /**
     * Constant for the F13 function key.
     *
     * @since 1.2
     */
    /* F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int F13            = 0xF000;

    /**
     * Constant for the F14 function key.
     *
     * @since 1.2
     */
    public static final int F14            = 0xF001;

    /**
     * Constant for the F15 function key.
     *
     * @since 1.2
     */
    public static final int F15            = 0xF002;

    /**
     * Constant for the F16 function key.
     *
     * @since 1.2
     */
    public static final int F16            = 0xF003;

    /**
     * Constant for the F17 function key.
     *
     * @since 1.2
     */
    public static final int F17            = 0xF004;

    /**
     * Constant for the F18 function key.
     *
     * @since 1.2
     */
    public static final int F18            = 0xF005;

    /**
     * Constant for the F19 function key.
     *
     * @since 1.2
     */
    public static final int F19            = 0xF006;

    /**
     * Constant for the F20 function key.
     *
     * @since 1.2
     */
    public static final int F20            = 0xF007;

    /**
     * Constant for the F21 function key.
     *
     * @since 1.2
     */
    public static final int F21            = 0xF008;

    /**
     * Constant for the F22 function key.
     *
     * @since 1.2
     */
    public static final int F22            = 0xF009;

    /**
     * Constant for the F23 function key.
     *
     * @since 1.2
     */
    public static final int F23            = 0xF00A;

    /**
     * Constant for the F24 function key.
     *
     * @since 1.2
     */
    public static final int F24            = 0xF00B;

    /**
     * Constant for the PRINTSCREEN key.
     */
    public static final int PRINTSCREEN    = 0x9A;

    /**
     * Constant for the INSERT key.
     */
    public static final int INSERT         = 0x9B;

    /**
     * Constant for the HELP key.
     */
    public static final int HELP           = 0x9C;

    /**
     * Constant for the META key.
     */
    public static final int META           = 0x9D;

    /**
     * Constant for the BACK_QUOTE  key.
     */
    public static final int BACK_QUOTE     = 0xC0;

    /**
     * Constant for the QUOTE key.
     */
    public static final int QUOTE          = 0xDE;

    /**
     * Constant for the numeric keypad <b>up</b> arrow key.
     *
     * @see #UP #UP
     * @since 1.2
     */
    public static final int KP_UP          = 0xE0;

    /**
     * Constant for the numeric keypad <b>down</b> arrow key.
     *
     * @see #DOWN #DOWN
     * @since 1.2
     */
    public static final int KP_DOWN        = 0xE1;

    /**
     * Constant for the numeric keypad <b>left</b> arrow key.
     *
     * @see #LEFT #LEFT
     * @since 1.2
     */
    public static final int KP_LEFT        = 0xE2;

    /**
     * Constant for the numeric keypad <b>right</b> arrow key.
     *
     * @see #RIGHT #RIGHT
     * @since 1.2
     */
    public static final int KP_RIGHT       = 0xE3;

    /* For European keyboards */
    /**
     * The constant DEAD_GRAVE.
     *
     * @since 1.2
     */
    public static final int DEAD_GRAVE               = 0x80;
    /**
     * The constant DEAD_ACUTE.
     *
     * @since 1.2
     */
    public static final int DEAD_ACUTE               = 0x81;
    /**
     * The constant DEAD_CIRCUMFLEX.
     *
     * @since 1.2
     */
    public static final int DEAD_CIRCUMFLEX          = 0x82;
    /**
     * The constant DEAD_TILDE.
     *
     * @since 1.2
     */
    public static final int DEAD_TILDE               = 0x83;
    /**
     * The constant DEAD_MACRON.
     *
     * @since 1.2
     */
    public static final int DEAD_MACRON              = 0x84;
    /**
     * The constant DEAD_BREVE.
     *
     * @since 1.2
     */
    public static final int DEAD_BREVE               = 0x85;
    /**
     * The constant DEAD_ABOVEDOT.
     *
     * @since 1.2
     */
    public static final int DEAD_ABOVEDOT            = 0x86;
    /**
     * The constant DEAD_DIAERESIS.
     *
     * @since 1.2
     */
    public static final int DEAD_DIAERESIS           = 0x87;
    /**
     * The constant DEAD_ABOVERING.
     *
     * @since 1.2
     */
    public static final int DEAD_ABOVERING           = 0x88;
    /**
     * The constant DEAD_DOUBLEACUTE.
     *
     * @since 1.2
     */
    public static final int DEAD_DOUBLEACUTE         = 0x89;
    /**
     * The constant DEAD_CARON.
     *
     * @since 1.2
     */
    public static final int DEAD_CARON               = 0x8a;
    /**
     * The constant DEAD_CEDILLA.
     *
     * @since 1.2
     */
    public static final int DEAD_CEDILLA             = 0x8b;
    /**
     * The constant DEAD_OGONEK.
     *
     * @since 1.2
     */
    public static final int DEAD_OGONEK              = 0x8c;
    /**
     * The constant DEAD_IOTA.
     *
     * @since 1.2
     */
    public static final int DEAD_IOTA                = 0x8d;
    /**
     * The constant DEAD_VOICED_SOUND.
     *
     * @since 1.2
     */
    public static final int DEAD_VOICED_SOUND        = 0x8e;
    /**
     * The constant DEAD_SEMIVOICED_SOUND.
     *
     * @since 1.2
     */
    public static final int DEAD_SEMIVOICED_SOUND    = 0x8f;

    /**
     * The constant AMPERSAND.
     *
     * @since 1.2
     */
    public static final int AMPERSAND                = 0x96;
    /**
     * The constant ASTERISK.
     *
     * @since 1.2
     */
    public static final int ASTERISK                 = 0x97;
    /**
     * The constant QUOTEDBL.
     *
     * @since 1.2
     */
    public static final int QUOTEDBL                 = 0x98;
    /**
     * The constant LESS.
     *
     * @since 1.2
     */
    public static final int LESS                     = 0x99;

    /**
     * The constant GREATER.
     *
     * @since 1.2
     */
    public static final int GREATER                  = 0xa0;
    /**
     * The constant BRACELEFT.
     *
     * @since 1.2
     */
    public static final int BRACELEFT                = 0xa1;
    /**
     * The constant BRACERIGHT.
     *
     * @since 1.2
     */
    public static final int BRACERIGHT               = 0xa2;

    /**
     * Constant for the "@" key.
     *
     * @since 1.2
     */
    public static final int AT                       = 0x0200;

    /**
     * Constant for the ":" key.
     *
     * @since 1.2
     */
    public static final int COLON                    = 0x0201;

    /**
     * Constant for the "^" key.
     *
     * @since 1.2
     */
    public static final int CIRCUMFLEX               = 0x0202;

    /**
     * Constant for the "$" key.
     *
     * @since 1.2
     */
    public static final int DOLLAR                   = 0x0203;

    /**
     * Constant for the Euro currency sign key.
     *
     * @since 1.2
     */
    public static final int EURO_SIGN                = 0x0204;

    /**
     * Constant for the "!" key.
     *
     * @since 1.2
     */
    public static final int EXCLAMATION_MARK         = 0x0205;

    /**
     * Constant for the inverted exclamation mark key.
     *
     * @since 1.2
     */
    public static final int INVERTED_EXCLAMATION_MARK = 0x0206;

    /**
     * Constant for the "(" key.
     *
     * @since 1.2
     */
    public static final int LEFT_PARENTHESIS         = 0x0207;

    /**
     * Constant for the "#" key.
     *
     * @since 1.2
     */
    public static final int NUMBER_SIGN              = 0x0208;

    /**
     * Constant for the "+" key.
     *
     * @since 1.2
     */
    public static final int PLUS                     = 0x0209;

    /**
     * Constant for the ")" key.
     *
     * @since 1.2
     */
    public static final int RIGHT_PARENTHESIS        = 0x020A;

    /**
     * Constant for the "_" key.
     *
     * @since 1.2
     */
    public static final int UNDERSCORE               = 0x020B;

    /**
     * Constant for the Microsoft Windows "Windows" key.
     * It is used for both the left and right version of the key.
     *
     * @since 1.5
     */
    public static final int WINDOWS                  = 0x020C;

    /**
     * Constant for the Microsoft Windows Context Menu key.
     *
     * @since 1.5
     */
    public static final int CONTEXT_MENU             = 0x020D;

    /* for input method support on Asian Keyboards */

    /* not clear what this means - listed in Microsoft Windows API */
    /**
     * Constant for the FINAL key.
     */
    public static final int FINAL                    = 0x0018;

    /**
     * Constant for the Convert function key.
     */
    /* Japanese PC 106 keyboard, Japanese Solaris keyboard: henkan */
    public static final int CONVERT                  = 0x001C;

    /**
     * Constant for the Don't Convert function key.
     */
    /* Japanese PC 106 keyboard: muhenkan */
    public static final int NONCONVERT               = 0x001D;

    /**
     * Constant for the Accept or Commit function key.
     */
    /* Japanese Solaris keyboard: kakutei */
    public static final int ACCEPT                   = 0x001E;

    /* not clear what this means - listed in Microsoft Windows API */
    /**
     * Constant for the MODECHANGE key.
     */
    public static final int MODECHANGE               = 0x001F;

    /* replaced by KANA_LOCK for Microsoft Windows and Solaris;
       might still be used on other platforms */
    /**
     * Constant for the KANA lock key.
     *
     * @see #KANA_LOCK #KANA_LOCK
     */
    public static final int KANA                     = 0x0015;

    /* replaced by INPUT_METHOD_ON_OFF for Microsoft Windows and Solaris;
       might still be used for other platforms */
    /**
     * Constant for KANJI.
     *
     * @see #INPUT_METHOD_ON_OFF #INPUT_METHOD_ON_OFF
     */
    public static final int KANJI                    = 0x0019;

    /**
     * Constant for the Alphanumeric function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: eisuu */
    public static final int ALPHANUMERIC             = 0x00F0;

    /**
     * Constant for the Katakana function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: katakana */
    public static final int KATAKANA                 = 0x00F1;

    /**
     * Constant for the Hiragana function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: hiragana */
    public static final int HIRAGANA                 = 0x00F2;

    /**
     * Constant for the Full-Width Characters function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: zenkaku */
    public static final int FULL_WIDTH               = 0x00F3;

    /**
     * Constant for the Half-Width Characters function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: hankaku */
    public static final int HALF_WIDTH               = 0x00F4;

    /**
     * Constant for the Roman Characters function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: roumaji */
    public static final int ROMAN_CHARACTERS         = 0x00F5;

    /**
     * Constant for the All Candidates function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - CONVERT + ALT: zenkouho */
    public static final int ALL_CANDIDATES           = 0x0100;

    /**
     * Constant for the Previous Candidate function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - CONVERT + SHIFT: maekouho */
    public static final int PREVIOUS_CANDIDATE       = 0x0101;

    /**
     * Constant for the Code Input function key.
     *
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - ALPHANUMERIC + ALT: kanji bangou */
    public static final int CODE_INPUT               = 0x0102;

    /**
     * Constant for the Japanese-Katakana function key.
     * This key switches to a Japanese input method and selects its Katakana input mode.
     *
     * @since 1.2
     */
    /* Japanese Macintosh keyboard - JAPANESE_HIRAGANA + SHIFT */
    public static final int JAPANESE_KATAKANA        = 0x0103;

    /**
     * Constant for the Japanese-Hiragana function key.
     * This key switches to a Japanese input method and selects its Hiragana input mode.
     *
     * @since 1.2
     */
    /* Japanese Macintosh keyboard */
    public static final int JAPANESE_HIRAGANA        = 0x0104;

    /**
     * Constant for the Japanese-Roman function key.
     * This key switches to a Japanese input method and selects its Roman-Direct input mode.
     *
     * @since 1.2
     */
    /* Japanese Macintosh keyboard */
    public static final int JAPANESE_ROMAN           = 0x0105;

    /**
     * Constant for the locking Kana function key.
     * This key locks the keyboard into a Kana layout.
     *
     * @since 1.3
     */
    /* Japanese PC 106 keyboard with special Windows driver - eisuu + Control; Japanese Solaris keyboard: kana */
    public static final int KANA_LOCK                = 0x0106;

    /**
     * Constant for the input method on/off key.
     *
     * @since 1.3
     */
    /* Japanese PC 106 keyboard: kanji. Japanese Solaris keyboard: nihongo */
    public static final int INPUT_METHOD_ON_OFF      = 0x0107;

    /* for Sun keyboards */
    /**
     * The constant CUT.
     *
     * @since 1.2
     */
    public static final int CUT                      = 0xFFD1;
    /**
     * The constant COPY.
     *
     * @since 1.2
     */
    public static final int COPY                     = 0xFFCD;
    /**
     * The constant PASTE.
     *
     * @since 1.2
     */
    public static final int PASTE                    = 0xFFCF;
    /**
     * The constant UNDO.
     *
     * @since 1.2
     */
    public static final int UNDO                     = 0xFFCB;
    /**
     * The constant AGAIN.
     *
     * @since 1.2
     */
    public static final int AGAIN                    = 0xFFC9;
    /**
     * The constant FIND.
     *
     * @since 1.2
     */
    public static final int FIND                     = 0xFFD0;
    /**
     * The constant PROPS.
     *
     * @since 1.2
     */
    public static final int PROPS                    = 0xFFCA;
    /**
     * The constant STOP.
     *
     * @since 1.2
     */
    public static final int STOP                     = 0xFFC8;

    /**
     * Constant for the Compose function key.
     *
     * @since 1.2
     */
    public static final int COMPOSE                  = 0xFF20;

    /**
     * Constant for the AltGraph function key.
     *
     * @since 1.2
     */
    public static final int ALT_GRAPH                = 0xFF7E;

    /**
     * Constant for the Begin key.
     *
     * @since 1.5
     */
    public static final int BEGIN                    = 0xFF58;

    /**
     * This value is used to indicate that the keyCode is unknown.
     * KEY_TYPED events do not have a keyCode value; this value
     * is used instead.
     */
    public static final int UNDEFINED      = 0x0;

    /**
     * KEY_PRESSED and KEY_RELEASED events which do not map to a
     * valid Unicode character use this for the keyChar value.
     */
    public static final char CHAR_UNDEFINED   = 0xFFFF;

    /* Mouse Codes */

    /**
     * Constant for the left click mouse button.
     */
    public static final int LEFT_CLICK = 1;

    /**
     * Constant for the middle mouse button.
     */
    public static final int MIDDLE_CLICK = 2;

    /**
     * Constant for the right mouse button.
     */
    public static final int RIGHT_CLICK = 3;

    private Harmony harmony;

    /**
     * The constant NUM_KEYS.
     */
    public static final int NUM_KEYS = 256;
    private static boolean[] keys       = new boolean[NUM_KEYS];
    private static boolean[] keysLast   = new boolean[NUM_KEYS];

    /**
     * The constant NUM_BUTTONS.
     */
    public static final int NUM_BUTTONS = 5;
    private static boolean[] buttons        = new boolean[NUM_BUTTONS];
    private static boolean[] buttonsLast    = new boolean[NUM_BUTTONS];

    private static Vector2f mouse = new Vector2f();
    private int scroll = 0;

    /**
     * Instantiates a new Input.
     *
     * @param harmony the harmony
     */
    public Input(Harmony harmony) {
        this.harmony = harmony;

        this.harmony.getDisplay().getCanvas().addKeyListener(this);
        this.harmony.getDisplay().getCanvas().addMouseListener(this);
        this.harmony.getDisplay().getCanvas().addMouseMotionListener(this);
        this.harmony.getDisplay().getCanvas().addMouseWheelListener(this);
    }

    /**
     * Update.
     */
    public void update() {
        System.arraycopy(keys, 0, keysLast, 0, NUM_KEYS);
        System.arraycopy(buttons, 0, buttonsLast, 0, NUM_BUTTONS);

        scroll = 0;
    }

    /**
     * Is key boolean.
     *
     * @param keycode the keycode
     * @return the boolean
     */
// Keys
    public static boolean isKey(int keycode) { return keys[keycode]; }

    /**
     * Is key up boolean.
     *
     * @param keycode the keycode
     * @return the boolean
     */
    public static boolean isKeyUp(int keycode) { return !keys[keycode] && keysLast[keycode]; }

    /**
     * Is key down boolean.
     *
     * @param keycode the keycode
     * @return the boolean
     */
    public static boolean isKeyDown(int keycode) { return keys[keycode] && !keysLast[keycode]; }

    /**
     * Is button boolean.
     *
     * @param button the button
     * @return the boolean
     */
// Buttons
    public static boolean isButton(int button) { return buttons[button]; }

    /**
     * Is button up boolean.
     *
     * @param button the button
     * @return the boolean
     */
    public static boolean isButtonUp(int button) { return !buttons[button] && buttonsLast[button]; }

    /**
     * Is button down boolean.
     *
     * @param button the button
     * @return the boolean
     */
    public static boolean isButtonDown(int button) { return buttons[button] && !buttonsLast[button]; }

    // Not Currently Being Used By The Input Class
    @Deprecated @Override public void keyTyped(KeyEvent e) {}
    @Deprecated @Override public void mouseClicked(MouseEvent e) {}
    @Deprecated @Override public void mouseEntered(MouseEvent e) {}
    @Deprecated @Override public void mouseExited(MouseEvent e) {}

    // Methods in use by the Input class
    @Override public void keyPressed(KeyEvent e) { keys[e.getKeyCode()] = true; }
    @Override public void keyReleased(KeyEvent e) { keys[e.getKeyCode()] = false; }

    @Override public void mousePressed(MouseEvent e) { buttons[e.getButton()] = true; }
    @Override public void mouseReleased(MouseEvent e) { buttons[e.getButton()] = false; }

    @Override public void mouseDragged(MouseEvent e) { mouse.set(e.getX(), e.getY()); }
    @Override public void mouseMoved(MouseEvent e) { mouse.set(e.getX(), e.getY()); }

    @Override public void mouseWheelMoved(MouseWheelEvent e) { scroll = e.getWheelRotation(); }

    /**
     * Gets the mouse position.
     *
     * @return the mouse
     */
    public Vector2f getMouse() { return mouse; }

    /**
     * Gets the current scroll.
     *
     * @return the scroll
     */
    public int getScroll() { return scroll; }
}