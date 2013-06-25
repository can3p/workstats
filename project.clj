(defproject workstats "0.1"
            :plugins [[lein-cljsbuild "0.3.0"]]
            :dependencies [[prismatic/dommy "0.1.1"]]
            :cljsbuild {
                        :builds {:background
                                 {
                                  :source-paths ["cljs/background"]
                                  :compiler {
                                             :output-to "background.js"
                                             :optimizations :whitespace
                                             :pretty-print true}}
                                 :popup
                                 {
                                  :source-paths ["cljs/popup"]
                                  :compiler {
                                             :output-to "popup.js"
                                             :optimizations :whitespace
                                             :pretty-print true}}}})
