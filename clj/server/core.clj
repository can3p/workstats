(ns server.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (route/files "/" { :root "./" })
  (route/not-found "Page not found"))

(def handler
  (handler/site app-routes))
