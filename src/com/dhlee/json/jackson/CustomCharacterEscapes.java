package com.dhlee.json.jackson;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

public class CustomCharacterEscapes extends CharacterEscapes {

	private final int[] _asciiEscapes;

    public CustomCharacterEscapes() {
        _asciiEscapes = standardAsciiEscapesForJSON();
        _asciiEscapes['/'] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return _asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int i) {
    	if(i == '/'){
            return new SerializableString() {

                @Override
                public String getValue() {
                    return "\\/";
                }

                @Override
                public int charLength() {
                    return 2;
                }

                @Override
                public char[] asQuotedChars() {
                    return new char[]{'\\','/'};
                }

                @Override
                public byte[] asUnquotedUTF8() {
                    return new byte[]{'\\','/'};
                }

                @Override
                public byte[] asQuotedUTF8() {
                    return new byte[]{'\\','/'};
                }

				@Override
				public int appendQuotedUTF8(byte[] buffer, int offset) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int appendQuoted(char[] buffer, int offset) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int appendUnquotedUTF8(byte[] buffer, int offset) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int appendUnquoted(char[] buffer, int offset) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int writeQuotedUTF8(OutputStream out) throws IOException {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int writeUnquotedUTF8(OutputStream out) throws IOException {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int putQuotedUTF8(ByteBuffer buffer) throws IOException {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int putUnquotedUTF8(ByteBuffer buffer) throws IOException {
					// TODO Auto-generated method stub
					return 0;
				}
            };
        }
        else{
            return null;
        }
    }
}
