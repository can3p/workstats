(defproject workstats "0.1"
            :plugins [
                      [lein-ring "0.8.5"]
                      [lein-cljsbuild "0.3.0"]]
            :dependencies [
                           [compojure "1.1.5"]
                           [org.clojure/clojure "1.5.1"]
                           [prismatic/dommy "0.1.1"]]
            :source-paths ["clj"]
            :ring {:handler server.core/handler}
            :cljsbuild {
                        :builds {
                                ;:repl
                                ;{
                                 ;:source-paths ["cljs/debug"]
                                 ;:compiler {
                                            ;:output-to "debug.js"
                                            ;:optimizations :whitespace
                                            ;:pretty-print true}}
                                 :background
                                 {
                                  :source-paths ["cljs/background"]
                                  :compiler {
                                             :output-to "background.js"
                                             :optimizations :whitespace
                                             :pretty-print true}}
                                 :popup
                                 {
                                  :source-paths ["cljs/popup", "cljs/debug"]
                                  :compiler {
                                             :output-to "popup.js"
                                             :optimizations :whitespace
                                             :pretty-print true}}}})
