(ns mine-sweep.ui.record.db
  (:require [cljs.spec.alpha :as spec]))

(spec/def ::username string?)
(spec/def ::order #{:latest :best})
(spec/def ::level #{:easy :medium :hard "easy" "medium" "hard"})
(spec/def ::inserted_at string?)
(spec/def ::record pos?)
(spec/def ::record-item (spec/keys :req-un [::level ::record]
                                   :opt-un [::inserted_at]))

(spec/def ::record-list (spec/coll-of ::record-item))

(spec/def ::level-records (spec/map-of ::level ::record-list))

(spec/def ::all-records (spec/map-of ::order ::level-records))

(spec/def :ui.record/records (spec/map-of ::username ::all-records))

(spec/def :ui.record/last-unsubmitted (spec/nilable ::record-item))
