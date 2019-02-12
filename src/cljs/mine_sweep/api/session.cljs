(ns mine-sweep.api.session
  (:require [ajax.core :refer [POST GET]]
            [mine-sweep.utils.log :refer [log]]))

(def session-url
  (if goog.DEBUG
    "http://localhost:4000/api/v1/session"
    "/api/v1/session"))

(def http-settings {:format :json
                    :response-format :json
                    :keywords? true})

(defn init-session
  [handler error-handler]
  (GET
   session-url
   (merge {:handler       handler
           :error-handler error-handler}
          http-settings)))

(defn new-record
  [data handler error-handler]
  (POST
   (str session-url "/record")
   (merge {:handler       handler
           :error-handler error-handler
           :params        data}
          http-settings)))

(defn get-records
  [{:keys [username] :as params} handler error-handler]
  (GET
   (str session-url "/records/user/" username)
   (merge  {:handler       handler
            :error-handler error-handler
            :url-params    (select-keys params [:order-by :n])}
           http-settings)))

(comment (get-records
          {:username "a" :n 2 :order-by :latest}
          nil nil))
