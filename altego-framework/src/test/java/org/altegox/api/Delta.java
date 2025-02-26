package org.altegox.api;

public class Delta {
        private String content;

        // Getter and Setter methods

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Delta{" +
                    "content='" + content + '\'' +
                    '}';
        }
    }