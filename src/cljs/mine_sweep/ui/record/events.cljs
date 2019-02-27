(ns mine-sweep.ui.record.events
  (:require [re-frame.core :as rf]
            [mine-sweep.utils.re-frame :refer [register-event-db
                                               register-event-fx]]
            [mine-sweep.api.session :as session-api]
            [mine-sweep.config :as config]
            [mine-sweep.utils.log :refer [log]]))

;; All time best is stored under a special key :all-time-best which is a keyword,
;; not a string; normal username is a stirng, so collisions can be avoided.
(register-event-db
 ::store-records
 (fn [db [_ records {:keys [username order-by]}]]
   (assoc-in db [:ui.record/records username order-by] records)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; get-all-time-best
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(register-event-fx
 :ui.record/get-all-time-best-records
 (fn [{db :db} [_ params]]
   {:db                        db
    ::server-get-all-time-best params}))

(rf/reg-fx
 ::server-get-all-time-best
 (fn [params]
   (session-api/get-all-time-best params
                                  #(rf/dispatch [::store-records (:data %) params])
                                  nil)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; get-records
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(register-event-fx
 :ui.record/get-records
 (fn [{db :db} [_ params]]
   {:db                  db
    ::server-get-records params}))

(rf/reg-fx
 ::server-get-records
 (fn [params]
   (session-api/get-records params
                            #(rf/dispatch [::store-records (:data %) params])
                            nil)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; create-record
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(register-event-fx
 :ui.record/create-record
 (fn [{db :db} _]
   {:db                    db
    ::server-create-record (:ui.record/last-unsubmitted db)}))

(defn- update-stored-records-by-username
  [db record username]
    (let [level       (-> record :level keyword)
          n-per-level ((if (string? username) :user :all-time-best) config/records-per-level)]
      (cond-> db
          ;; add new record in the best list, sort by record and take first n.
        true
        (update-in [:ui.record/records username :best level]
                   #(as-> % rs
                      (conj rs record)
                      (sort-by :record rs)
                      (take n-per-level rs)
                      (vec rs)))

        (when-not (= username :all-time-best))
          ;; add new record in the latest list at the beginning, and take first n.
          (update-in [:ui.record/records username :latest level]
                     #(->> %
                       (into [record])
                       (take n-per-level)
                       vec)))))

(defn- update-stored-records
  [db new-record]
  (let [username (-> db :ui.auth/session :username)]
    (reduce #(update-stored-records-by-username %1 new-record %2)
            db
            [username :all-time-best])))

(comment (update-stored-records
          {:ui.auth/session {:username "a"}
           :ui.record/records {"a" {:best {:easy [{:inserted_at "2019-02-12T14:40:41" :level "easy" :record 78}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 127}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 137}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 147}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 157}]}
                                    :latest {}}}}
          {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 100}))

(register-event-db
 ::create-record-success
 (fn [db [_ new-record]]
   (-> db
       (update-stored-records new-record)
       (assoc :ui.record/last-unsubmitted nil))))

(rf/reg-fx
 ::server-create-record
 (fn [params]
   (when params
     (session-api/new-record
      params
      #(rf/dispatch [::create-record-success (:data %)])
      nil))))

(comment (session-api/new-record
          {:record 188 :level :easy}
          nil nil))
