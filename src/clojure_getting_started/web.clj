(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [clojure.string :as string]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from xwefwefHeroku"})

(defn page
  [page-name]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (let [layout (slurp (io/resource "app/layout.html.erb"))
               sidebar (slurp (io/resource "app/_sidebar.html.erb"))
               content (string/replace
                         (slurp (io/resource (format "app/%s.html.erb" page-name)))
                         #"SIDEBAR" sidebar)]
           (string/replace layout #"YIELD" content))})

(defroutes app
  (GET "/" []
    (page "index"))
  (GET "/about" []
    (page "about"))
  (GET "/contact" []
    (page "contact"))
  (route/resources "/")
  #_(route/not-found (slurp (io/resource "404.html"))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
(comment
  (.stop server)
  (def server (-main)))
