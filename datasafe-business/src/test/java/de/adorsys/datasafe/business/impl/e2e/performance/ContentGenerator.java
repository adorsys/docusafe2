package de.adorsys.datasafe.business.impl.e2e.performance;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class ContentGenerator {

    private final int size;

    public InputStream generate(String pattern) {
        return new Input(size, pattern);
    }

    @RequiredArgsConstructor
    private static class Input extends InputStream {

        private final AtomicInteger sizeRemaining;
        private final char[] pattern;

        public Input(int size, String pattern) {
            if (size < pattern.length()) {
                throw new IllegalArgumentException("Not enough size for pattern: " + pattern);
            }

            this.sizeRemaining = new AtomicInteger(size);
            this.pattern = pattern.toCharArray();
        }

        @Override
        public int read() {
            int sz = sizeRemaining.decrementAndGet();
            if (sz == 0) {
                return -1;
            }

            return pattern[sz % pattern.length];
        }
    }
}
