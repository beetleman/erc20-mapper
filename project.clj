(defproject erc20-mapper "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :local-repo "./.m2"
;;  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :dependencies [[ch.qos.logback/logback-classic "1.2.3"]
                 [clj-time "0.14.2"]
                 [compojure "1.6.0"]
                 [conman "0.7.7"]
                 [cprop "0.1.11"]
                 [funcool/struct "1.2.0"]
                 [luminus-aleph "0.1.5"]
                 [luminus-migrations "0.5.0"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.2"]

                 ;; [metosin/compojure-api "1.1.12"]
                 [metosin/spec-tools "0.6.1"]
                 [metosin/compojure-api "2.0.0-alpha19"]

                 [metosin/muuntaja "0.5.0"]
                 [metosin/ring-http-response "0.9.0"]
                 [mount "0.1.12"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.postgresql/postgresql "42.2.2"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [selmer "1.11.7"]]

  :min-lein-version "2.0.0"

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot erc20-mapper.core

  :plugins []

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "erc20-mapper.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-server" "-Dconf=dev-config.edn"]
                  :dependencies [[pjstadig/humane-test-output "0.8.3"]
                                 [prone "1.5.1"]
                                 [ring/ring-devel "1.6.3"]
                                 [ring/ring-mock "0.3.2"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.22.0"]
                                 [lein-kibit "0.1.6"]
                                 [lein-ancient "0.6.15"]
                                 ;; for cider repl
                                 [refactor-nrepl "2.4.0-SNAPSHOT"]
                                 [cider/cider-nrepl "0.17.0-SNAPSHOT"]]

                  :source-paths ["env/dev/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-server" "-Dconf=test-config.edn"]
                  :resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}}

  :repl-options
  {:host "0.0.0.0"
   :port 7000}

  :aliases
  {"repl-dev"   ["do"
                 ["clean"]
                 ["repl" ":headless"]]})
