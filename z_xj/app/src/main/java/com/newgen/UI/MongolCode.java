package com.newgen.UI;


/*
 * Mongol Code
 *
 * Updated for Unicode 10.0 standards
 * http://unicode.org/charts/PDF/U1800.pdf
 * Deviating from Unicode 10.0 for
 *    - MONGOLIAN LETTER GA first and second form final (matching DS01, needed to
  *     break context) (So words with only I default to feminine. Menksoft also
  *     does this.)
 *    - MONGOLIAN LETTER I, third medial form. Undefined in Unicode 10. This is a
  *     single tooth I after a vowel. Needed to break context. Menksoft also
  *     does this.
 *
 * The purpose of this class is to render Unicode text into glyphs
 * that can be displayed on all versions of Android. It solves the
 * problem of Mongolian script not being supported before Android 6.0,
 * and problems with Unicode rendering after Android 6.0.
 *
 * Current version needs to be used with Menksoft font glyphs located
 * in the PUA starting at \uE234. It is recommended that all external
 * text use Unicode. However, Menksoft code can also be converted back
 * into Unicode.
 */
public final class MongolCode {

    // this is a singleton class (should it just be a static class?)
    public final static MongolCode INSTANCE = new MongolCode();




    public enum Location {
        ISOLATE, INITIAL, MEDIAL, FINAL
    }

    public enum Gender {
        MASCULINE, FEMININE, NEUTER
    }

    // Constructor
    private MongolCode() {
    }

    public String unicodeToMenksoft(CharSequence inputString) {

        if (inputString == null) return null;
        if (inputString.length() == 0) return "";

        StringBuilder outputString = new StringBuilder();
        StringBuilder mongolWord = new StringBuilder();

        // Loop through characters in string
        int length = inputString.length();
        for (int i = 0; i < length; i++) {
            final char character = inputString.charAt(i);
            if (isMongolian(character)) {
                mongolWord.append(character);
                continue;
            }

            if (mongolWord.length() > 0) {
                appendMongolWord(outputString, mongolWord);
                mongolWord.setLength(0);
            }

            // NNBS starts a new Mongol word but is not itself a Mongol char
            if (character == Uni.NNBS) {
                mongolWord.append(Uni.NNBS);
                continue;
            }

            // non-Mongol character
            outputString.append(character);
        }

        // Add any final substring
        if (mongolWord.length() > 0)
            appendMongolWord(outputString, mongolWord);

        return outputString.toString();
    }

    private void appendMongolWord(StringBuilder outputString, StringBuilder mongolWord) {
        String renderedWord = new MongolWord(mongolWord).convertToMenksoftCode();
        outputString.append(renderedWord);
    }

    public String menksoftToUnicode(String inputString) {
        final char space = ' ';
        StringBuilder outputString = new StringBuilder();

        if (inputString == null || inputString.length() == 0) {
            return "";
        }

        // Loop through characters in string
        int length = inputString.length();
        for (int i = 0; i < length; i++) {

            char currentChar = inputString.charAt(i);

            if (!isMenksoft(currentChar)) {
                if (currentChar == space && i < length - 1) {
                    switch (inputString.charAt(i + 1)) {
                        case Glyph.MEDI_A_FVS2:
                        case Glyph.FINA_I:
                        case Glyph.MEDI_I:
                        case Glyph.MEDI_I_SUFFIX:
                        case Glyph.ISOL_I_SUFFIX:
                        case Glyph.MEDI_O:
                        case Glyph.MEDI_O_BP:
                        case Glyph.FINA_O:
                        case Glyph.MEDI_U:
                        case Glyph.MEDI_U_BP:
                        case Glyph.FINA_U:
                        case Glyph.MEDI_OE:
                        case Glyph.MEDI_OE_BP:
                        case Glyph.FINA_OE:
                        case Glyph.MEDI_UE:
                        case Glyph.MEDI_UE_BP:
                        case Glyph.FINA_UE:
                        case Glyph.FINA_YA:
                        case Glyph.INIT_YA_FVS1:
                            outputString.append(Uni.NNBS);
                            break;
                        default:
                            outputString.append(space);
                    }
                } else {
                    outputString.append(currentChar);
                }
                continue;
            }

            // TODO check if glyph location type with actual location
            // If there is a mismatch then add ZWJ

            if (currentChar < Glyph.A_START) {                         // punctuation
                switch (currentChar) {
                    case Glyph.BIRGA:
                        outputString.append(Uni.MONGOLIAN_BIRGA);
                        break;
                    case Glyph.ELLIPSIS:
                        outputString.append(Uni.MONGOLIAN_ELLIPSIS);
                        break;
                    case Glyph.COMMA:
                        outputString.append(Uni.MONGOLIAN_COMMA);
                        break;
                    case Glyph.FULL_STOP:
                        outputString.append(Uni.MONGOLIAN_FULL_STOP);
                        break;
                    case Glyph.COLON:
                        outputString.append(Uni.MONGOLIAN_COLON);
                        break;
                    case Glyph.FOUR_DOTS:
                        outputString.append(Uni.MONGOLIAN_FOUR_DOTS);
                        break;
                    case Glyph.TODO_SOFT_HYPHEN:
                        outputString.append(Uni.MONGOLIAN_TODO_SOFT_HYPHEN);
                        break;
                    case Glyph.SIBE_SYLLABLE_BOUNDARY_MARKER:
                        outputString.append(Uni.MONGOLIAN_SIBE_SYLLABLE_BOUNDARY_MARKER);
                        break;
                    case Glyph.MANCH_COMMA:
                        outputString.append(Uni.MONGOLIAN_MANCHU_COMMA);
                        break;
                    case Glyph.MANCHU_FULL_STOP:
                        outputString.append(Uni.MONGOLIAN_MANCHU_FULL_STOP);
                        break;
                    case Glyph.NIRUGU:
                        outputString.append(Uni.MONGOLIAN_NIRUGU);
                        break;
                    case Glyph.BIRGA_WITH_ORNAMENT:
                        outputString.append("\uD805\uDE60"); // U+11660
                        break;
                    case Glyph.ROTATED_BIRGA:
                        outputString.append("\uD805\uDE61"); // U+11661
                        break;
                    case Glyph.DOUBLE_BIRGA_WITH_ORNAMENT:
                        outputString.append("\uD805\uDE62"); // U+11662
                        break;
                    case Glyph.TRIPLE_BIRGA_WITH_ORNAMENT:
                        outputString.append("\uD805\uDE63"); // U+11663
                        break;
                    case Glyph.MIDDLE_DOT:
                        outputString.append(Uni.MIDDLE_DOT);
                        break;
                    case Glyph.ZERO:
                        outputString.append(Uni.MONGOLIAN_DIGIT_ZERO);
                        break;
                    case Glyph.ONE:
                        outputString.append(Uni.MONGOLIAN_DIGIT_ONE);
                        break;
                    case Glyph.TWO:
                        outputString.append(Uni.MONGOLIAN_DIGIT_TWO);
                        break;
                    case Glyph.THREE:
                        outputString.append(Uni.MONGOLIAN_DIGIT_THREE);
                        break;
                    case Glyph.FOUR:
                        outputString.append(Uni.MONGOLIAN_DIGIT_FOUR);
                        break;
                    case Glyph.FIVE:
                        outputString.append(Uni.MONGOLIAN_DIGIT_FIVE);
                        break;
                    case Glyph.SIX:
                        outputString.append(Uni.MONGOLIAN_DIGIT_SIX);
                        break;
                    case Glyph.SEVEN:
                        outputString.append(Uni.MONGOLIAN_DIGIT_SEVEN);
                        break;
                    case Glyph.EIGHT:
                        outputString.append(Uni.MONGOLIAN_DIGIT_EIGHT);
                        break;
                    case Glyph.NINE:
                        outputString.append(Uni.MONGOLIAN_DIGIT_NINE);
                        break;
                    case Glyph.QUESTION_EXCLAMATION:
                        outputString.append(Uni.QUESTION_EXCLAMATION_MARK);
                        break;
                    case Glyph.EXCLAMATION_QUESTION:
                        outputString.append(Uni.EXCLAMATION_QUESTION_MARK);
                        break;
                    case Glyph.EXCLAMATION:
                        outputString.append(Uni.VERTICAL_EXCLAMATION_MARK);
                        break;
                    case Glyph.QUESTION:
                        outputString.append(Uni.VERTICAL_QUESTION_MARK);
                        break;
                    case Glyph.SEMICOLON:
                        outputString.append(Uni.VERTICAL_SEMICOLON);
                        break;
                    case Glyph.LEFT_PARENTHESIS:
                        outputString.append(Uni.VERTICAL_LEFT_PARENTHESIS);
                        break;
                    case Glyph.RIGHT_PARENTHESIS:
                        outputString.append(Uni.VERTICAL_RIGHT_PARENTHESIS);
                        break;
                    case Glyph.LEFT_ANGLE_BRACKET:
                        outputString.append(Uni.VERTICAL_LEFT_ANGLE_BRACKET);
                        break;
                    case Glyph.RIGHT_ANGLE_BRACKET:
                        outputString.append(Uni.VERTICAL_RIGHT_ANGLE_BRACKET);
                        break;
                    case Glyph.LEFT_BRACKET:
                        outputString.append(Uni.VERTICAL_LEFT_SQUARE_BRACKET);
                        break;
                    case Glyph.RIGHT_BRACKET:
                        outputString.append(Uni.VERTICAL_RIGHT_SQUARE_BRACKET);
                        break;
                    case Glyph.LEFT_DOUBLE_ANGLE_BRACKET:
                        outputString.append(Uni.VERTICAL_LEFT_DOUBLE_ANGLE_BRACKET);
                        break;
                    case Glyph.RIGHT_DOUBLE_ANGLE_BRACKET:
                        outputString.append(Uni.VERTICAL_RIGHT_DOUBLE_ANGLE_BRACKET);
                        break;
                    case Glyph.LEFT_WHITE_CORNER_BRACKET:
                        outputString.append(Uni.VERTICAL_LEFT_WHITE_CORNER_BRACKET);
                        break;
                    case Glyph.RIGHT_WHITE_CORNER_BRACKET:
                        outputString.append(Uni.VERTICAL_RIGHT_WHITE_CORNER_BRACKET);
                        break;
                    case Glyph.FULLWIDTH_COMMA:
                        outputString.append(Uni.VERTICAL_COMMA);
                        break;
                    case Glyph.X:
                        outputString.append('\u00D7'); // FIXME using the multiplication sign?
                        break;
                    case Glyph.REFERENCE_MARK:
                        outputString.append(Uni.REFERENCE_MARK);
                        break;
                    case Glyph.EN_DASH:
                        outputString.append(Uni.VERTICAL_EN_DASH);
                        break;
                    case Glyph.EM_DASH:
                        outputString.append(Uni.VERTICAL_EM_DASH);
                        break;
                    default:
                        outputString.append(currentChar);
                }
            } else if (currentChar < Glyph.E_START) {                  // A
                switch (currentChar) {
                    case Glyph.ISOL_A_FVS1:
                    case Glyph.MEDI_A_FVS1:
                        outputString.append(Uni.A);
                        outputString.append(Uni.FVS1);
                        break;
                    case Glyph.MEDI_A_FVS2:
                        outputString.append(Uni.A);
                        outputString.append(Uni.FVS2);
                        break;
                    case Glyph.FINA_A_MVS:
                        if (outputString.length() > 0 &&
                                outputString.charAt(outputString.length() - 1) != Uni.MVS) {
                            outputString.append(Uni.MVS);
                        }
                        outputString.append(Uni.A);
                        break;
                    default:
                        outputString.append(Uni.A);
                }
            } else if (currentChar < Glyph.I_START) {                  // E
                switch (currentChar) {
                    case Glyph.INIT_E_FVS1:
                        outputString.append(Uni.E);
                        outputString.append(Uni.FVS1);
                        break;
                    case Glyph.FINA_E_MVS:
                        if (outputString.length() > 0 &&
                                outputString.charAt(outputString.length() - 1) != Uni.MVS) {
                            outputString.append(Uni.MVS);
                        }
                        outputString.append(Uni.E);
                        break;
                    default:
                        outputString.append(Uni.E);
                }
            } else if (currentChar < Glyph.O_START) {                  // I

                switch (currentChar) {
                    case Glyph.MEDI_I_FVS1:
                        outputString.append(Uni.I);
                        outputString.append(Uni.FVS1);
                        break;
                    case Glyph.MEDI_I_DOUBLE_TOOTH:
                        outputString.append(Uni.YA);
                        outputString.append(Uni.I);
                        break;
                    default:
                        outputString.append(Uni.I);
                }
            } else if (currentChar < Glyph.U_START) {                  // O
                outputString.append(Uni.O);
                switch (currentChar) {
                    case Glyph.MEDI_O_FVS1:
                    case Glyph.FINA_O_FVS1:
                        outputString.append(Uni.FVS1);
                        break;
                }
            } else if (currentChar < Glyph.OE_START) {                 // U
                outputString.append(Uni.U);
                if (currentChar == Glyph.MEDI_U_FVS1) {
                    outputString.append(Uni.FVS1);
                }
            } else if (currentChar < Glyph.UE_START) {                 // OE
                outputString.append(Uni.OE);
                switch (currentChar) {
                    case Glyph.FINA_OE_FVS1:
                        outputString.append(Uni.FVS1);
                        break;
                    case Glyph.MEDI_OE_FVS2:
                        outputString.append(Uni.FVS2);
                        break;
                }
            } else if (currentChar < Glyph.EE_START) {                 // UE
                outputString.append(Uni.UE);
                switch (currentChar) {
                    case Glyph.ISOL_UE_FVS1:
                    case Glyph.FINA_UE_FVS1:
                        outputString.append(Uni.FVS1);
                        break;
                    case Glyph.MEDI_UE_FVS2:
                        outputString.append(Uni.FVS2);
                        break;
                }
            } else if (currentChar < Glyph.NA_START) {                 // EE
                outputString.append(Uni.EE);
            } else if (currentChar < Glyph.BA_START) {                 // NA and ANG
                // handling these together because NA glyphs
                // are split in the Menksoft code.
                if (currentChar >= Glyph.ANG_START &&
                        currentChar <= Glyph.ANG_END) {
                    outputString.append(Uni.ANG);
                } else {
                    outputString.append(Uni.NA);
                    switch (currentChar) {
                        case Glyph.MEDI_NA_FVS2:
                            outputString.append(Uni.MVS);
                            break;
                        case Glyph.INIT_NA_FVS1_TOOTH:
                        case Glyph.INIT_NA_FVS1_STEM:
                            outputString.append(Uni.FVS1);
                            break;
                    }
                }
            } else if (currentChar < Glyph.PA_START) {                 // BA
                outputString.append(Uni.BA);
                if (currentChar == Glyph.FINA_BA_FVS1) {
                    outputString.append(Uni.FVS1);
                }
            } else if (currentChar < Glyph.QA_START) {                 // PA
                outputString.append(Uni.PA);
            } else if (currentChar < Glyph.GA_START) {                 // QA
                switch (currentChar) {
                    // treat the dotted masculine Q as a G
                    // ignoring all ancient dotted feminine forms
                    case Glyph.INIT_QA_FVS1_TOOTH:
                    case Glyph.INIT_QA_FVS1_STEM:
                    case Glyph.MEDI_QA_FVS1:
                    case Glyph.MEDI_QA_FVS2:
                        outputString.append(Uni.GA);
                        break;
                    // If a medial Q is being used like a G before
                    // a consonant, then interpret it as a G.
                    case Glyph.MEDI_QA_TOOTH:
                    case Glyph.MEDI_QA_STEM:
                    case Glyph.MEDI_QA_FEM_CONSONANT:
                        if (i < length - 1 && isMenksoftConsonant(inputString.charAt(i + 1))) {
                            outputString.append(Uni.GA);
                        } else {
                            outputString.append(Uni.QA);
                        }
                        break;
                    default:
                        outputString.append(Uni.QA);
                        break;
                }
            } else if (currentChar < Glyph.MA_START) {                 // GA
                switch (currentChar) {
                    // treat the undotted masculine G as a Q
                    case Glyph.INIT_GA_FVS1_TOOTH:
                    case Glyph.INIT_GA_FVS1_STEM:
                        outputString.append(Uni.QA);
                        break;
                    case Glyph.MEDI_GA_FVS2:
                        outputString.append(Uni.GA);
                        outputString.append(Uni.MVS);
                        break;
                    default:
                        outputString.append(Uni.GA);
                        break;
                }
            } else if (currentChar < Glyph.LA_START) {                 // MA
                outputString.append(Uni.MA);
            } else if (currentChar < Glyph.SA_START) {                 // LA
                outputString.append(Uni.LA);
            } else if (currentChar < Glyph.SHA_START) {                // SA
                outputString.append(Uni.SA);
                switch (currentChar) {
                    case Glyph.FINA_SA_FVS1:
                        outputString.append(Uni.FVS1);
                        break;
                    case Glyph.FINA_SA_FVS2:
                        outputString.append(Uni.FVS2);
                        break;
                }
            } else if (currentChar < Glyph.TA_START) {                 // SHA
                outputString.append(Uni.SHA);
            } else if (currentChar < Glyph.DA_START) {                 // TA
                outputString.append(Uni.TA);
                switch (currentChar) {
                    case Glyph.MEDI_TA_FVS1_STEM:
                    case Glyph.MEDI_TA_FVS1_TOOTH:
                        outputString.append(Uni.FVS1);
                        break;
                }
            } else if (currentChar < Glyph.CHA_START) {                // DA
                outputString.append(Uni.DA);
                switch (currentChar) {
                    case Glyph.INIT_DA_FVS1:
                    case Glyph.FINA_DA_FVS1:
                        outputString.append(Uni.FVS1);
                        break;
                }
            } else if (currentChar < Glyph.JA_START) {                 // CHA
                outputString.append(Uni.CHA);
            } else if (currentChar < Glyph.YA_START) {                 // JA
                outputString.append(Uni.JA);
            } else if (currentChar < Glyph.RA_START) {                 // YA
                outputString.append(Uni.YA);
                // TODO add FVS1 for dipthongs?
                if (currentChar == Glyph.INIT_YA_FVS1) {
                    outputString.append(Uni.FVS1);
                }
            } else if (currentChar < Glyph.WA_START) {                 // RA
                outputString.append(Uni.RA);
            } else if (currentChar < Glyph.FA_START) {                 // WA
                outputString.append(Uni.WA);
            } else if (currentChar < Glyph.KA_START) {                 // FA
                outputString.append(Uni.FA);
            } else if (currentChar < Glyph.KHA_START) {                // KA
                outputString.append(Uni.KA);
            } else if (currentChar < Glyph.TSA_START) {                // KHA
                outputString.append(Uni.KHA);
            } else if (currentChar < Glyph.ZA_START) {                 // TSA
                outputString.append(Uni.TSA);
            } else if (currentChar < Glyph.HAA_START) {                // ZA
                outputString.append(Uni.ZA);
            } else if (currentChar < Glyph.ZRA_START) {                // HAA
                outputString.append(Uni.HAA);
            } else if (currentChar < Glyph.LHA_START) {                // ZRA
                outputString.append(Uni.ZRA);
            } else if (currentChar < Glyph.ZHI_START) {                // LHA
                outputString.append(Uni.LHA);
            } else if (currentChar < Glyph.CHI_START) {                // ZHI
                outputString.append(Uni.ZHI);
            } else if (currentChar <= Glyph.MENKSOFT_END) {            // CHI
                outputString.append(Uni.CHI);
            }
        }

        return outputString.toString();
    }

    public static Location getLocation(CharSequence textBefore, CharSequence textAfter) {

        // TODO should we be using this in convertWordToMenksoftCode?

        boolean beforeIsMongolian = false;
        boolean afterIsMongolian = false;

        int length = textBefore.length();
        if (length > 0 && isMongolian(textBefore.charAt(length - 1))) {
            beforeIsMongolian = true;
        }

        length = textAfter.length();

        for (int i = 0; i < length; i++) {
            char currentChar = textAfter.charAt(i);
            if (isFVS(currentChar) || currentChar == Uni.MVS) {
                continue;
            } else if (isMongolian(currentChar)) {
                afterIsMongolian = true;
            }
            break;
        }

        if (beforeIsMongolian && afterIsMongolian) return Location.MEDIAL;
        else if (!beforeIsMongolian && afterIsMongolian) return Location.INITIAL;
        else if (beforeIsMongolian) return Location.FINAL;
        else return Location.ISOLATE;
    }

    private boolean isMenksoftConsonant(char character) {
        return character >= Glyph.NA_START && character <= Glyph.FINA_CHI;
    }

    public boolean isMenksoft(char character) {
        return character >= Glyph.MENKSOFT_START && character <= Glyph.MENKSOFT_END;
    }

    public static boolean isVowel(char character) {
        return (character >= Uni.A && character <= Uni.EE);
    }

    public static boolean isMasculineVowel(char character) {
        return (character == Uni.A || character == Uni.O || character == Uni.U);
    }

    public static boolean isFeminineVowel(char character) {
        return (character == Uni.E || character == Uni.EE || character == Uni.OE || character == Uni.UE);
    }

    public static boolean isConsonant(char character) {
        return (character >= Uni.NA && character <= Uni.CHI);
    }

    public static boolean isFVS(char character) {
        return (character >= Uni.FVS1 && character <= Uni.FVS3);
    }

    public static boolean isMongolian(char character) {
        // Mongolian letters, MVS, FVS1-3, NIRUGU, Uni.ZWJ, (but not NNBS)
        return ((character >= Uni.A && character <= Uni.CHI)
                || (character >= Uni.MONGOLIAN_NIRUGU && character <= Uni.MVS) || character == Uni.ZWJ);
    }

    // MVS, FVS, ZWJ
    static boolean isRenderedGlyph(int index, CharSequence someString) {
        final char someChar = someString.charAt(index);
        return !(someChar == Uni.MVS || isFVS(someChar) || someChar == Uni.ZWJ);
    }

    private static boolean isBGDRS(char character) {
        // This method is not used internally, only for external use.
        return (character == Uni.BA || character == Uni.GA || character == Uni.DA
                || character == Uni.RA || character == Uni.SA);
    }

    public static boolean isMvsConsonant(char character) {
        return (character == Uni.NA || character == Uni.QA || character == Uni.GA
                || character == Uni.MA || character == Uni.LA || character == Uni.JA
                || character == Uni.YA || character == Uni.RA || character == Uni.WA);
    }

//    private static boolean isMongolianAlphabet(char character) {
//        // This method is not used internally, only for external use.
//        return (character >= Uni.A && character <= Uni.CHI);
//    }

    // YIN comes after a vowel, UN comes after a consonant, U comes after N.
    static String getSuffixYinUnU(Gender previousWordGender, char previousWordLastChar) {
        if (isVowel(previousWordLastChar)) {
            return Suffix.YIN;
        } else if (previousWordLastChar == Uni.NA) {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.U;
            } else {
                return Suffix.UE;
            }
        } else {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.UN;
            } else {
                return Suffix.UEN;
            }
        }
    }

    // TU after B, G, D, R, S. Others are DU.
    static String getSuffixTuDu(Gender previousWordGender, char previousWordLastChar) {
        if (isBGDRS(previousWordLastChar)) {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.TU;
            } else {
                return Suffix.TUE;
            }
        } else {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.DU;
            } else {
                return Suffix.DUE;
            }
        }
    }


    // Yi comes after a vowel, I comes after a consonant.
    static String getSuffixYiI(char previousWordLastChar) {
        if (isVowel(previousWordLastChar)) {
            return Suffix.YI;
        }
        return Suffix.I;
    }

    // BAR comes after a vowel, IYAR comes after a consonant.
    static String getSuffixBarIyar(Gender previousWordGender, char previousWordLastChar) {
        if (isVowel(previousWordLastChar)) {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.IYAR;
            } else {
                return Suffix.IYER;
            }
        } else {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.BAR;
            } else {
                return Suffix.BER;
            }
        }
    }

    // BAN comes after a vowel, IYAN comes after a consonant.
    static String getSuffixBanIyan(Gender previousWordGender, char previousWordLastChar) {
        if (isVowel(previousWordLastChar)) {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.IYAN;
            } else {
                return Suffix.IYEN;
            }
        } else {
            if (previousWordGender == Gender.MASCULINE) {
                return Suffix.BAN;
            } else {
                return Suffix.BEN;
            }
        }
    }

    static String getSuffixAchaEche(Gender previousWordGender) {
        if (previousWordGender == Gender.MASCULINE) {
            return Suffix.ACHA;
        } else {
            return Suffix.ECHE;
        }
    }

    static String getSuffixTaiTei(Gender previousWordGender) {
        if (previousWordGender == Gender.MASCULINE) {
            return Suffix.TAI;
        } else {
            return Suffix.TEI;
        }
    }

    static String getSuffixUu(Gender previousWordGender) {
        if (previousWordGender == Gender.MASCULINE) {
            return Suffix.UU;
        } else {
            return Suffix.UEUE;
        }
    }

    static String getSuffixUd(Gender previousWordGender) {
        if (previousWordGender == Gender.MASCULINE) {
            return Suffix.UD;
        } else {
            return Suffix.UED;
        }
    }

    static String getSuffixNugud(Gender previousWordGender) {
        if (previousWordGender == Gender.MASCULINE) {
            return Suffix.NUGUD;
        } else {
            return Suffix.NUEGUED;
        }
    }

    // Starts at the end of the word and works up
    // if mixed genders only reports the first one from the bottom
    // returns null if word does not end in a valid Mongolian character
    static Gender getWordGender(String word) {
        return MongolWord.getGender(word);
    }

    public class Uni {

        public static final char ZWNJ = '\u200C'; // Zero-width non joiner
        public static final char ZWJ = '\u200D'; // Zero-width joiner
        public static final char NNBS = '\u202F'; // Narrow No-Break Space

        // vertical presentation forms
        public static final char VERTICAL_COMMA = '\uFE10';
        public static final char VERTICAL_IDEOGRAPHIC_COMMA = '\uFE11';
        public static final char VERTICAL_IDEOGRAPHIC_FULL_STOP = '\uFE12';
        public static final char VERTICAL_COLON = '\uFE13';
        public static final char VERTICAL_SEMICOLON = '\uFE14';
        public static final char VERTICAL_EXCLAMATION_MARK = '\uFE15';
        public static final char VERTICAL_QUESTION_MARK = '\uFE16';
        public static final char VERTICAL_LEFT_WHITE_LENTICULAR_BRACKET = '\uFE17';
        public static final char VERTICAL_RIGHT_WHITE_LENTICULAR_BRAKCET = '\uFE18';
        public static final char VERTICAL_HORIZONTAL_ELLIPSIS = '\uFE19';
        public static final char VERTICAL_TWO_DOT_LEADER = '\uFE30';
        public static final char VERTICAL_EM_DASH = '\uFE31';
        public static final char VERTICAL_EN_DASH = '\uFE32';
        public static final char VERTICAL_LOW_LINE = '\uFE33';
        public static final char VERTICAL_WAVY_LOW_LINE = '\uFE34';
        public static final char VERTICAL_LEFT_PARENTHESIS = '\uFE35';
        public static final char VERTICAL_RIGHT_PARENTHESIS = '\uFE36';
        public static final char VERTICAL_LEFT_CURLY_BRACKET = '\uFE37';
        public static final char VERTICAL_RIGHT_CURLY_BRACKET = '\uFE38';
        public static final char VERTICAL_LEFT_TORTOISE_SHELL_BRACKET = '\uFE39';
        public static final char VERTICAL_RIGHT_TORTOISE_SHELL_BRACKET = '\uFE3A';
        public static final char VERTICAL_LEFT_BLACK_LENTICULAR_BRACKET = '\uFE3B';
        public static final char VERTICAL_RIGHT_BLACK_LENTICULAR_BRACKET = '\uFE3C';
        public static final char VERTICAL_LEFT_DOUBLE_ANGLE_BRACKET = '\uFE3D';
        public static final char VERTICAL_RIGHT_DOUBLE_ANGLE_BRACKET = '\uFE3E';
        public static final char VERTICAL_LEFT_ANGLE_BRACKET = '\uFE3F';
        public static final char VERTICAL_RIGHT_ANGLE_BRACKET = '\uFE40';
        public static final char VERTICAL_LEFT_CORNER_BRACKET = '\uFE41';
        public static final char VERTICAL_RIGHT_CORNER_BRACKET = '\uFE42';
        public static final char VERTICAL_LEFT_WHITE_CORNER_BRACKET = '\uFE43';
        public static final char VERTICAL_RIGHT_WHITE_CORNER_BRACKET = '\uFE44';
        public static final char VERTICAL_LEFT_SQUARE_BRACKET = '\uFE47';
        public static final char VERTICAL_RIGHT_SQUARE_BRACKET = '\uFE48';

        // Other
        public static final char MIDDLE_DOT = '\u00B7';
        public static final char REFERENCE_MARK = '\u203B';
        public static final char DOUBLE_EXCLAMATION_MARK = '\u203C';
        public static final char DOUBLE_QUESTION_MARK = '\u2047';
        public static final char QUESTION_EXCLAMATION_MARK = '\u2048';
        public static final char EXCLAMATION_QUESTION_MARK = '\u2049';


        // Unicode Mongolian Values
        public static final char MONGOLIAN_BIRGA = '\u1800';
        public static final char MONGOLIAN_ELLIPSIS = '\u1801';
        public static final char MONGOLIAN_COMMA = '\u1802';
        public static final char MONGOLIAN_FULL_STOP = '\u1803';
        public static final char MONGOLIAN_COLON = '\u1804';
        public static final char MONGOLIAN_FOUR_DOTS = '\u1805';
        public static final char MONGOLIAN_TODO_SOFT_HYPHEN = '\u1806';
        public static final char MONGOLIAN_SIBE_SYLLABLE_BOUNDARY_MARKER = '\u1807';
        public static final char MONGOLIAN_MANCHU_COMMA = '\u1808';
        public static final char MONGOLIAN_MANCHU_FULL_STOP = '\u1809';
        public static final char MONGOLIAN_NIRUGU = '\u180a';
        public static final char FVS1 = '\u180b';
        public static final char FVS2 = '\u180c';
        public static final char FVS3 = '\u180d';
        public static final char MVS = '\u180e'; // MONGOLIAN_VOWEL_SEPARATOR
        public static final char MONGOLIAN_DIGIT_ZERO = '\u1810';
        public static final char MONGOLIAN_DIGIT_ONE = '\u1811';
        public static final char MONGOLIAN_DIGIT_TWO = '\u1812';
        public static final char MONGOLIAN_DIGIT_THREE = '\u1813';
        public static final char MONGOLIAN_DIGIT_FOUR = '\u1814';
        public static final char MONGOLIAN_DIGIT_FIVE = '\u1815';
        public static final char MONGOLIAN_DIGIT_SIX = '\u1816';
        public static final char MONGOLIAN_DIGIT_SEVEN = '\u1817';
        public static final char MONGOLIAN_DIGIT_EIGHT = '\u1818';
        public static final char MONGOLIAN_DIGIT_NINE = '\u1819';
        public static final char A = '\u1820'; // MONGOLIAN_LETTER_xx
        public static final char E = '\u1821';
        public static final char I = '\u1822';
        public static final char O = '\u1823';
        public static final char U = '\u1824';
        public static final char OE = '\u1825';
        public static final char UE = '\u1826';
        public static final char EE = '\u1827';
        public static final char NA = '\u1828';
        public static final char ANG = '\u1829';
        public static final char BA = '\u182A';
        public static final char PA = '\u182B';
        public static final char QA = '\u182C';
        public static final char GA = '\u182D';
        public static final char MA = '\u182E';
        public static final char LA = '\u182F';
        public static final char SA = '\u1830';
        public static final char SHA = '\u1831';
        public static final char TA = '\u1832';
        public static final char DA = '\u1833';
        public static final char CHA = '\u1834';
        public static final char JA = '\u1835';
        public static final char YA = '\u1836';
        public static final char RA = '\u1837';
        public static final char WA = '\u1838';
        public static final char FA = '\u1839';
        public static final char KA = '\u183A';
        public static final char KHA = '\u183B';
        public static final char TSA = '\u183C';
        public static final char ZA = '\u183D';
        public static final char HAA = '\u183E';
        public static final char ZRA = '\u183F';
        public static final char LHA = '\u1840';
        public static final char ZHI = '\u1841';
        public static final char CHI = '\u1842';
    }

    public class Suffix {
        public static final String YIN = "\u202F\u1836\u1822\u1828";
        public static final String UN = "\u202F\u1824\u1828";
        public static final String UEN = "\u202F\u1826\u1828";
        public static final String U = "\u202F\u1824";
        public static final String UE = "\u202F\u1826";
        public static final String I = "\u202F\u1822";
        public static final String YI = "\u202F\u1836\u1822";
        public static final String DU = "\u202F\u1833\u1824";
        public static final String DUE = "\u202F\u1833\u1826";
        public static final String TU = "\u202F\u1832\u1824";
        public static final String TUE = "\u202F\u1832\u1826";
        public static final String DUR = "\u202F\u1833\u1824\u1837";
        public static final String DUER = "\u202F\u1833\u1826\u1837";
        public static final String TUR = "\u202F\u1832\u1824\u1837";
        public static final String TUER = "\u202F\u1832\u1826\u1837";
        public static final String DAQI = "\u202F\u1833\u1820\u182C\u1822";
        public static final String DEQI = "\u202F\u1833\u1821\u182C\u1822";
        public static final String ACHA = "\u202F\u1820\u1834\u1820";
        public static final String ECHE = "\u202F\u1821\u1834\u1821";
        public static final String BAR = "\u202F\u182A\u1820\u1837";
        public static final String BER = "\u202F\u182A\u1821\u1837";
        public static final String IYAR = "\u202F\u1822\u1836\u1820\u1837";
        public static final String IYER = "\u202F\u1822\u1836\u1821\u1837";
        public static final String TAI = "\u202F\u1832\u1820\u1822";
        public static final String TEI = "\u202F\u1832\u1821\u1822";
        public static final String LUGA = "\u202F\u182F\u1824\u182D\u180E\u1820";
        public static final String LUEGE = "\u202F\u182F\u1826\u182D\u1821";
        public static final String BAN = "\u202F\u182A\u1820\u1828";
        public static final String BEN = "\u202F\u182A\u1821\u1828";
        public static final String IYAN = "\u202F\u1822\u1836\u1820\u1828";
        public static final String IYEN = "\u202F\u1822\u1836\u1821\u1828";
        public static final String YUGAN = "\u202F\u1836\u1824\u182D\u1820\u1828";
        public static final String YUEGEN = "\u202F\u1836\u1826\u182D\u1821\u1828";
        public static final String DAGAN = "\u202F\u1833\u1820\u182D\u1820\u1828";
        public static final String DEGEN = "\u202F\u1833\u1821\u182D\u1821\u1828";
        public static final String TAGAN = "\u202F\u1832\u1820\u182D\u1820\u1828";
        public static final String TEGEN = "\u202F\u1832\u1821\u182D\u1821\u1828";
        public static final String ACHAGAN = "\u202F\u1820\u1834\u1820\u182D\u1820\u1828";
        public static final String ECHEGEN = "\u202F\u1821\u1834\u1821\u182D\u1821\u1828";
        public static final String TAIGAN = "\u202F\u1832\u1820\u1822\u182D\u1820\u1828";
        public static final String TEIGEN = "\u202F\u1832\u1821\u1822\u182D\u1821\u1828";
        public static final String UD = "\u202F\u1824\u1833";
        public static final String UED = "\u202F\u1826\u1833";
        public static final String NUGUD = "\u202F\u1828\u1824\u182D\u1824\u1833";
        public static final String NUEGUED = "\u202F\u1828\u1826\u182D\u1826\u1833";
        public static final String NAR = "\u202F\u1828\u1820\u1837";
        public static final String NER = "\u202F\u1828\u1821\u1837";
        public static final String UU = "\u202F\u1824\u1824";
        public static final String UEUE = "\u202F\u1826\u1826";
        public static final String DA = "\u202F\u1833\u1820";
        public static final String DE = "\u202F\u1833\u1821";

        // TODO should we add others?
        // urugu?
    }

    class Glyph {

        static final char MENKSOFT_START = '\uE234';
        static final char MENKSOFT_END = '\uE34F';

        // Private Use Area glyph values
        static final char BIRGA = '\uE234';
        static final char ELLIPSIS = '\uE235';
        static final char COMMA = '\uE236';
        static final char FULL_STOP = '\uE237';
        static final char COLON = '\uE238';
        static final char FOUR_DOTS = '\uE239';
        static final char TODO_SOFT_HYPHEN = '\uE23A';
        static final char SIBE_SYLLABLE_BOUNDARY_MARKER = '\uE23B';
        static final char MANCH_COMMA = '\uE23C';
        static final char MANCHU_FULL_STOP = '\uE23D';
        static final char NIRUGU = '\uE23E';
        static final char BIRGA_WITH_ORNAMENT = '\uE23F';
        static final char ROTATED_BIRGA = '\uE240';
        static final char DOUBLE_BIRGA_WITH_ORNAMENT = '\uE241';
        static final char TRIPLE_BIRGA_WITH_ORNAMENT = '\uE242';
        static final char MIDDLE_DOT = '\uE243';
        static final char ZERO = '\uE244';
        static final char ONE = '\uE245';
        static final char TWO = '\uE246';
        static final char THREE = '\uE247';
        static final char FOUR = '\uE248';
        static final char FIVE = '\uE249';
        static final char SIX = '\uE24A';
        static final char SEVEN = '\uE24B';
        static final char EIGHT = '\uE24C';
        static final char NINE = '\uE24D';
        static final char QUESTION_EXCLAMATION = '\uE24E';
        static final char EXCLAMATION_QUESTION = '\uE24F';
        static final char EXCLAMATION = '\uE250';
        static final char QUESTION = '\uE251';
        static final char SEMICOLON = '\uE252';
        static final char LEFT_PARENTHESIS = '\uE253';
        static final char RIGHT_PARENTHESIS = '\uE254';
        static final char LEFT_ANGLE_BRACKET = '\uE255';
        static final char RIGHT_ANGLE_BRACKET = '\uE256';
        static final char LEFT_BRACKET = '\uE257';
        static final char RIGHT_BRACKET = '\uE258';
        static final char LEFT_DOUBLE_ANGLE_BRACKET = '\uE259';
        static final char RIGHT_DOUBLE_ANGLE_BRACKET = '\uE25A';
        static final char LEFT_WHITE_CORNER_BRACKET = '\uE25B';
        static final char RIGHT_WHITE_CORNER_BRACKET = '\uE25C';
        static final char FULLWIDTH_COMMA = '\uE25D';
        static final char X = '\uE25E';
        static final char REFERENCE_MARK = '\uE25F';                   // 0x203b
        static final char EN_DASH = '\uE260'; // TODO is that what this is?
        static final char EM_DASH = '\uE261'; // TODO is that what this is?

        // These are in the order of the Unicode 9 specs sheet
        // BP = looks better after B, P (and other rounded like Q, G, F, K, KH)
        // MVS = final glyph varient for MVS
        // gv = glyph varient, same basic glyph form as the one it follows.
        // TOOTH = the ending of this character matches a following character that slants left (for example, a tooth)
        // STEM = the ending of this character matches a following character that starts with a vertical stem
        // ROUND = the ending of this character matches a round following character (feminine QG)
        static final char A_START = '\uE264';
        static final char ISOL_A = '\uE264';
        static final char INIT_A = '\uE266';
        static final char MEDI_A = '\uE26C';
        static final char MEDI_A_BP = '\uE26D'; // gv
        static final char FINA_A = '\uE268';
        static final char FINA_A_BP = '\uE26B'; // final A following BPKF
        static final char ISOL_A_FVS1 = '\uE265';
        static final char MEDI_A_FVS1 = '\uE26E';
        static final char FINA_A_FVS1 = '\uE269';
        static final char FINA_A_MVS = '\uE26A'; // gv for MVS + A
        static final char MEDI_A_FVS2 = '\uE267'; // A of ACHA suffix

        static final char E_START = '\uE270';
        static final char ISOL_E = '\uE270';
        static final char INIT_E = '\uE271';
        static final char MEDI_E = '\uE276';
        static final char MEDI_E_BP = '\uE277';
        static final char FINA_E = '\uE273';
        static final char FINA_E_BP = '\uE275'; // final E following BPKF
        static final char INIT_E_FVS1 = '\uE272';
        static final char FINA_E_FVS1 = '\uE269'; // no E glyph so using A
        static final char FINA_E_MVS = '\uE274'; // gv for MVS + E

        static final char I_START = '\uE279';
        static final char ISOL_I = '\uE279';
        static final char ISOL_I_SUFFIX = '\uE282';
        static final char INIT_I = '\uE27A';
        static final char MEDI_I = '\uE27E';
        static final char MEDI_I_SUFFIX = '\uE280';
        static final char MEDI_I_BP = '\uE27F'; // gv
        static final char MEDI_I_DOUBLE_TOOTH = '\uE281'; // gv
        static final char FINA_I = '\uE27B';
        static final char FINA_I_BP = '\uE27C'; // gv
        static final char MEDI_I_FVS1 = '\uE27D'; //

        static final char O_START = '\uE283';
        static final char ISOL_O = '\uE283';
        static final char INIT_O = '\uE284';
        static final char MEDI_O = '\uE289';
        static final char MEDI_O_BP = '\uE28A';
        static final char FINA_O = '\uE285';
        static final char FINA_O_BP = '\uE287'; // gv
        static final char MEDI_O_FVS1 = '\uE288';
        static final char FINA_O_FVS1 = '\uE286';

        static final char U_START = '\uE28B';  // Using Init U gliph
        static final char ISOL_U = '\uE28C';  // Using Init U gliph
        static final char INIT_U = '\uE28C';
        static final char MEDI_U = '\uE291';
        static final char MEDI_U_BP = '\uE292'; // gv
        static final char FINA_U = '\uE28D';
        static final char FINA_U_BP = '\uE28F'; // gv
        static final char MEDI_U_FVS1 = '\uE290';
        static final char FINA_U_FVS1 = '\uE28E';  // FIXME not defined in Unicode 10.0

        static final char OE_START = '\uE293';
        static final char ISOL_OE = '\uE293';
        static final char INIT_OE = '\uE295';
        static final char MEDI_OE = '\uE29E';
        static final char MEDI_OE_BP = '\uE29F'; // gv
        static final char FINA_OE = '\uE296';
        static final char FINA_OE_BP = '\uE29A'; // gv
        static final char MEDI_OE_FVS1 = '\uE29C';
        static final char MEDI_OE_FVS1_BP = '\uE29D';
        static final char FINA_OE_FVS1 = '\uE297';
        static final char FINA_OE_FVS1_BP = '\uE298'; // gv
        static final char MEDI_OE_FVS2 = '\uE29B';

        static final char UE_START = '\uE2A0';
        static final char ISOL_UE = '\uE2A2'; // Using initial glyph
        static final char INIT_UE = '\uE2A2';
        static final char MEDI_UE = '\uE2AB';
        static final char MEDI_UE_BP = '\uE2AC';
        static final char FINA_UE = '\uE2A3';
        static final char FINA_UE_BP = '\uE2A7';
        static final char ISOL_UE_FVS1 = '\uE2A1';
        static final char MEDI_UE_FVS1 = '\uE2A9';
        static final char MEDI_UE_FVS1_BP = '\uE2AA';
        static final char FINA_UE_FVS1 = '\uE2A4';
        static final char FINA_UE_FVS1_BP = '\uE2A5';
        static final char MEDI_UE_FVS2 = '\uE2A8';

        static final char EE_START = '\uE2AD';
        static final char ISOL_EE = '\uE2AD';
        static final char INIT_EE = '\uE2AE';
        static final char MEDI_EE = '\uE2B0';
        static final char FINA_EE = '\uE2AF';

        static final char NA_START = '\uE2B1';
        static final char ISOL_NA = '\uE2B3';
        static final char INIT_NA_TOOTH = '\uE2B1';
        static final char INIT_NA_STEM = '\uE2B3';
        static final char MEDI_NA_TOOTH = '\uE2B8';
        static final char MEDI_NA_STEM = '\uE2BA';
        static final char FINA_NA = '\uE2B5';
        static final char INIT_NA_FVS1_TOOTH = '\uE2B2';
        static final char INIT_NA_FVS1_STEM = '\uE2B4';
        static final char MEDI_NA_FVS1_TOOTH = '\uE2B7';
        static final char MEDI_NA_FVS1_STEM = '\uE2B9';
        //static final char MEDI_NA_FVS1_NG = '\uE2BF'; // What is this one for?
        static final char MEDI_NA_FVS2 = '\uE2B6'; // MVS
        static final char MEDI_NA_FVS3 = '\uE2B7'; // Tod Mongol N; FIXME: no glyph, substituting medial dotted n

        static final char ANG_START = '\uE2BB';
        static final char ISOL_ANG = '\uE2BC';
        static final char INIT_ANG_TOOTH = '\uE2BC';
        static final char INIT_ANG_ROUND = '\uE2BD';
        static final char INIT_ANG_STEM = '\uE2BE';
        static final char MEDI_ANG_TOOTH = '\uE2BC'; // good for following tooth
        static final char MEDI_ANG_ROUND = '\uE2BD'; // good for following round letter like B P H K
        static final char MEDI_ANG_STEM = '\uE2BE'; // good for following stem letter like J CH R
        static final char FINA_ANG = '\uE2BB';
        static final char ANG_END = '\uE2BE';

        static final char BA_START = '\uE2C1';
        static final char ISOL_BA = '\uE2C1';
        static final char INIT_BA = '\uE2C1';
        static final char INIT_BA_OU = '\uE2C2';
        static final char INIT_BA_STEM = '\uE2C7'; // using medial stem
        static final char MEDI_BA_TOOTH = '\uE2C5';
        static final char MEDI_BA_OU = '\uE2C6';
        static final char MEDI_BA_STEM = '\uE2C7';
        static final char FINA_BA = '\uE2C3';
        static final char FINA_BA_FVS1 = '\uE2C4';

        static final char PA_START = '\uE2C8';
        static final char ISOL_PA = '\uE2C8';
        static final char INIT_PA = '\uE2C8';
        static final char INIT_PA_OU = '\uE2C9';
        static final char INIT_PA_STEM = '\uE2CD'; // using medial stem
        static final char MEDI_PA_TOOTH = '\uE2CB';
        static final char MEDI_PA_OU = '\uE2CC';
        static final char MEDI_PA_STEM = '\uE2CD';
        static final char FINA_PA = '\uE2CA';

        static final char QA_START = '\uE2CE';
        static final char ISOL_QA = '\uE2D2';
        static final char INIT_QA_TOOTH = '\uE2CE';
        static final char INIT_QA_STEM = '\uE2D2';
        static final char INIT_QA_FEM = '\uE2D0'; // GV FEMININE
        static final char INIT_QA_FEM_OU = '\uE2D4';
        static final char MEDI_QA_TOOTH = '\uE2D8';
        static final char MEDI_QA_STEM = '\uE2DC';
        static final char MEDI_QA_FEM = '\uE2DA';
        static final char MEDI_QA_FEM_CONSONANT = '\uE2DF';
        static final char MEDI_QA_FEM_CONSONANT_DOTTED = '\uE2E0';
        static final char MEDI_QA_FEM_OU = '\uE2DD';
        static final char FINA_QA = '\uE2D6';
        static final char INIT_QA_FVS1_TOOTH = '\uE2CF';
        static final char INIT_QA_FVS1_STEM = '\uE2D3';
        static final char INIT_QA_FVS1_FEM = '\uE2D1';
        static final char INIT_QA_FVS1_FEM_OU = '\uE2D5';
        static final char MEDI_QA_FVS1 = '\uE2D9';
        static final char MEDI_QA_FVS1_FEM = '\uE2DB';
        static final char MEDI_QA_FVS1_FEM_OU = '\uE2DE';
        static final char ISOL_QA_FVS1 = '\uE2D1'; // feminine with 2 dots
        static final char MEDI_QA_FVS2 = '\uE2D7';
        static final char MEDI_QA_FVS3 = '\uE2D6';

        static final char GA_START = '\uE2E1';
        static final char ISOL_GA = '\uE2E4';
        static final char INIT_GA_TOOTH = '\uE2E1';
        static final char INIT_GA_STEM = '\uE2E4';
        static final char INIT_GA_FEM = '\uE2E3';
        static final char INIT_GA_FEM_OU = '\uE2E6';
        static final char MEDI_GA = '\uE2EE';
        static final char MEDI_GA_FEM = '\uE2EB';
        static final char MEDI_GA_FEM_OU = '\uE2ED';
        static final char FINA_GA = '\uE2E7';
        static final char INIT_GA_FVS1_TOOTH = '\uE2E2';
        static final char INIT_GA_FVS1_STEM = '\uE2E5';
        static final char MEDI_GA_FVS1_TOOTH = '\uE2EA';
        static final char MEDI_GA_FVS1_STEM = '\uE2EC';
        // This deviation is necessary to override context rules.
        // This follows the WG2 decision: https://r12a.github.io/mongolian-variants/
        static final char FINA_GA_FVS1 = '\uE2E7'; // masculine context override FIXME Deviating from Unicode 10.0 !!!
        static final char FINA_GA_FVS2 = '\uE2E8'; // feminine final form FIXME Deviating from Unicode 10.0 !!!
        static final char MEDI_GA_FVS2 = '\uE2E9';
        static final char MEDI_GA_FVS3_TOOTH = '\uE2EF';
        static final char MEDI_GA_FVS3_STEM = '\uE2F0';

        static final char MA_START = '\uE2F1';
        static final char ISOL_MA = '\uE2F2';
        static final char INIT_MA_TOOTH = '\uE2F1';
        static final char INIT_MA_STEM_LONG = '\uE2F2';
        static final char MEDI_MA_TOOTH = '\uE2F4';
        static final char MEDI_MA_STEM_LONG = '\uE2F5'; // long stem GV, use this if M or L follows
        static final char MEDI_MA_BP = '\uE2F6'; // GV use this if following a B, P, H, K, etc.
        static final char FINA_MA = '\uE2F3';

        static final char LA_START = '\uE2F7';
        static final char ISOL_LA = '\uE2F8';
        static final char INIT_LA_TOOTH = '\uE2F7';
        static final char INIT_LA_STEM_LONG = '\uE2F8';
        static final char MEDI_LA_TOOTH = '\uE2FA';
        static final char MEDI_LA_STEM_LONG = '\uE2FB'; // long stem GV, use this if M or L follows
        static final char MEDI_LA_BP = '\uE2FC'; // GV use this if following a B, P, H, K, etc.
        static final char FINA_LA = '\uE2F9';

        static final char SA_START = '\uE2FD';
        static final char ISOL_SA = '\uE2FE';
        static final char INIT_SA_TOOTH = '\uE2FD';
        static final char INIT_SA_STEM = '\uE2FE';
        static final char MEDI_SA_TOOTH = '\uE301';
        static final char MEDI_SA_STEM = '\uE302';
        static final char FINA_SA = '\uE2FF';
        static final char FINA_SA_FVS1 = '\uE300';
        static final char FINA_SA_FVS2 = '\uE2FF'; //0x100CE; FIXME: glyph is not in Menksoft PUA, substituting first form

        static final char SHA_START = '\uE303';
        static final char ISOL_SHA = '\uE304';
        static final char INIT_SHA_TOOTH = '\uE303';
        static final char INIT_SHA_STEM = '\uE304';
        static final char MEDI_SHA_TOOTH = '\uE306';
        static final char MEDI_SHA_STEM = '\uE307';
        static final char FINA_SHA = '\uE305';

        static final char TA_START = '\uE308';
        static final char ISOL_TA = '\uE309';
        static final char INIT_TA_TOOTH = '\uE308';
        static final char INIT_TA_STEM = '\uE309';
        static final char MEDI_TA = '\uE30B';
        static final char FINA_TA = '\uE30A';
        static final char MEDI_TA_FVS1_TOOTH = '\uE30C';
        static final char MEDI_TA_FVS1_STEM = '\uE30D';

        static final char DA_START = '\uE30E';
        static final char ISOL_DA = '\uE310';
        static final char INIT_DA_TOOTH = '\uE30E';
        static final char INIT_DA_STEM = '\uE30F';
        static final char MEDI_DA = '\uE314';
        static final char FINA_DA = '\uE311';
        static final char INIT_DA_FVS1 = '\uE310';
        static final char MEDI_DA_FVS1 = '\uE313';
        static final char FINA_DA_FVS1 = '\uE312';

        static final char CHA_START = '\uE315';
        static final char ISOL_CHA = '\uE315';
        static final char INIT_CHA = '\uE315';
        static final char MEDI_CHA = '\uE317';
        static final char FINA_CHA = '\uE316';

        static final char JA_START = '\uE318';
        static final char ISOL_JA = '\uE318';
        static final char INIT_JA_TOOTH = '\uE319';
        static final char INIT_JA_STEM = '\uE31A';
        static final char MEDI_JA = '\uE31D';
        static final char FINA_JA = '\uE31B';
        static final char MEDI_JA_FVS1 = '\uE31C'; // MVS

        static final char YA_START = '\uE31E';
        static final char ISOL_YA = '\uE31E';
        static final char INIT_YA = '\uE31E';
        //static final char MEDI_YA = '\uE320'; // hooked (Unicode 9.0)
        static final char MEDI_YA = '\uE321'; // straight (Unicode 10.0)
        static final char FINA_YA = '\uE31F';
        static final char INIT_YA_FVS1 = '\uE321';
        //static final char MEDI_YA_FVS1 = '\uE321'; // straight (Unicode 9.0)
        static final char MEDI_YA_FVS1 = '\uE320'; // hooked (Unicode 10.0)
        static final char MEDI_YA_FVS2 = '\uE31F';

        static final char RA_START = '\uE322';
        static final char ISOL_RA = '\uE322';
        static final char INIT_RA_TOOTH = '\uE323';
        static final char INIT_RA_STEM = '\uE322';
        static final char MEDI_RA_TOOTH = '\uE327';
        static final char MEDI_RA_STEM = '\uE326';
        static final char FINA_RA = '\uE325';

        static final char WA_START = '\uE329';
        static final char ISOL_WA = '\uE329';
        static final char INIT_WA = '\uE329';
        static final char MEDI_WA = '\uE32C';
        static final char FINA_WA = '\uE32A';
        static final char FINA_WA_FVS1 = '\uE32B'; // MVS

        static final char FA_START = '\uE32D';
        static final char ISOL_FA = '\uE32D';
        static final char INIT_FA = '\uE32D';
        static final char INIT_FA_OU = '\uE32E';
        static final char INIT_FA_STEM = '\uE332'; // using medial stem
        static final char MEDI_FA_TOOTH = '\uE330';
        static final char MEDI_FA_OU = '\uE331';
        static final char MEDI_FA_STEM = '\uE332';
        static final char FINA_FA = '\uE32F';

        static final char KA_START = '\uE333';
        static final char ISOL_KA = '\uE333';
        static final char INIT_KA = '\uE333';
        static final char INIT_KA_OU = '\uE334';
        static final char MEDI_KA_TOOTH = '\uE336';
        static final char MEDI_KA_OU = '\uE337';
        static final char MEDI_KA_STEM = '\uE338';
        static final char FINA_KA = '\uE335';

        static final char KHA_START = '\uE339';
        static final char ISOL_KHA = '\uE339';
        static final char INIT_KHA = '\uE339';
        static final char INIT_KHA_OU = '\uE33A';
        static final char MEDI_KHA_TOOTH = '\uE33C';
        static final char MEDI_KHA_OU = '\uE33D';
        static final char MEDI_KHA_STEM = '\uE33E';
        static final char FINA_KHA = '\uE33B';

        static final char TSA_START = '\uE33F';
        static final char ISOL_TSA = '\uE33F';
        static final char INIT_TSA = '\uE33F';
        static final char MEDI_TSA = '\uE341';
        static final char FINA_TSA = '\uE340';

        static final char ZA_START = '\uE342';
        static final char ISOL_ZA = '\uE342';
        static final char INIT_ZA = '\uE342';
        static final char MEDI_ZA = '\uE344';
        static final char FINA_ZA = '\uE343';

        static final char HAA_START = '\uE345';
        static final char ISOL_HAA = '\uE345';
        static final char INIT_HAA = '\uE345';
        static final char MEDI_HAA = '\uE347';
        static final char FINA_HAA = '\uE346';

        static final char ZRA_START = '\uE348';
        static final char ISOL_ZRA = '\uE348';
        static final char INIT_ZRA = '\uE348';
        static final char MEDI_ZRA = '\uE349';
        static final char FINA_ZRA = '\uE34A';

        static final char LHA_START = '\uE34B';
        static final char ISOL_LHA = '\uE34B';
        static final char INIT_LHA = '\uE34B';
        static final char MEDI_LHA = '\uE34C';
        static final char MEDI_LHA_BP = '\uE34D';
        static final char FINA_LHA = '\uE34C';
        static final char FINA_LHA_BP = '\uE34D';

        static final char ZHI_START = '\uE34E';
        static final char ISOL_ZHI = '\uE34E';
        static final char INIT_ZHI = '\uE34E';
        static final char MEDI_ZHI = '\uE34E';
        static final char FINA_ZHI = '\uE34E';

        static final char CHI_START = '\uE34F';
        static final char ISOL_CHI = '\uE34F';
        static final char INIT_CHI = '\uE34F';
        static final char MEDI_CHI = '\uE34F';
        static final char FINA_CHI = '\uE34F';

    }
}
