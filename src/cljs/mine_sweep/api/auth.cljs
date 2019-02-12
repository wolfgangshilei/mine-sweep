(ns mine-sweep.api.auth
  (:require [ajax.core :refer [POST GET]]))

(def auth-api
  (if goog.DEBUG
    "http://localhost:4000/api/v1/auth"
    "/api/v1/auth"))

(def http-settings {:format :json
                    :response-format :json
                    :keywords? true})

(def login-url (str auth-api "/login"))
(defn login
  [form handler error-handler]

  (POST
   login-url
   (merge {:handler       handler
           :error-handler error-handler
           :params        form}
          http-settings)))

(def signup-url (str auth-api "/signup"))
(defn signup
  [form handler error-handler]
  (POST
   signup-url
   (merge {:handler       handler
           :error-handler error-handler
           :params        form}
          http-settings)))
