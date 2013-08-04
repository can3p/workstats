(ns popup
  (:use-macros [dommy.macros :only [sel1]])
  (:require 
    [lib.timer :as timer]
    [lib.results
     :refer [store-result! get-latest!]]
    [dommy.core
     :refer [listen! add-class! remove-class! set-html!]]
    [lib.util :as util]))

(def timer-node (sel1 :.timer-current-time))
(def body (sel1 :.timer-body))
(def pause (sel1 :.timer-pause))
(def restart (sel1 :.timer-reset))
(def time-struct (atom (timer/create)))

(defn set-time! [time]
  (let [
        time-str (util/format-time time)]
    (set-html! timer-node time-str)))

(defn update-time []
  (when (timer/started? @time-struct)
    (set-time! (timer/get-length @time-struct))
    (js/setTimeout update-time 1000)))

(defn restore-result []
  (let [result (get-latest!)]
    (when result
      (reset! time-struct result))))

(defn set-timer-state! [state]
  (if (= state :paused)
    (do
      (remove-class! body :timer-runs)
      (add-class! body :timer-paused))
    (do
      (add-class! body :timer-runs)
      (remove-class! body :timer-paused))))

(listen! pause :click (fn [] (if (timer/started? @time-struct)
                           (do
                             (set-timer-state! :paused)
                             (timer/stop! time-struct))
                           (do
                             (set-timer-state! :runs)
                             (timer/start! time-struct)
                             (update-time)))))

(listen! restart :click (fn []
                      (timer/stop! time-struct)
                      (store-result! time-struct)
                      (timer/create! time-struct)
                      (set-timer-state! :runs)
                      (update-time)))

(listen! js/window :unload (fn []
                             (store-result! time-struct "latest")))

(restore-result)

(if-not (timer/started? @time-struct)
  (set-timer-state! :paused))

(when time-struct
  (set-time! (timer/get-length @time-struct)))

(update-time)
