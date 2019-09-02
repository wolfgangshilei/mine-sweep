
(defproject mine-sweep "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.312"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [funcool/cuerdas "2.0.5"]
                 [garden  "1.3.6"]
                 [stylefy "1.11.0"]
                 [cljs-ajax "0.8.0"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :repl-eval-timeout 60000}

  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [figwheel-sidecar "0.5.16"]
                   [cider/piggieback "0.3.5"]
                   [re-frisk "0.5.3"]
                   [org.clojure/test.check "0.9.0"]]
    :plugins      [[lein-figwheel "0.5.16"]
                   [lein-doo "0.1.8"]]}
   :prod { }
   }

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "mine-sweep.core/mount-root"}
     :compiler     {:main                 mine-sweep.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           re-frisk.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    :install-deps         true
                    :npm-deps             {:react-transition-group "2.5.3"}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            mine-sweep.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          mine-sweep.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none
                    :target        :nodejs}}
    ]}
  )
