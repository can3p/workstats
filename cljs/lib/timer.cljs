(ns lib.timer)

(defn get-current []
  (.getTime (new js/Date)))

(defn get-length [timer]
  (let [
        time (get-current)
        sum (if (:sum timer)
              (:sum timer)
              0)
        delta (if (:last timer)
                (- time (:last timer))
                0)]
    (+ sum delta)))

(defn stop [timer]
  (let [{:keys (last ranges sum)} timer
        time (get-current)]
    (if-not (started? timer)
      timer
      (assoc timer
             :last nil
             :ranges (conj ranges [last time])
             :sum (+ sum (- time last))))))

(defn start [timer]
  (if (started? timer)
    timer
    (assoc timer
           :last (get-current))))

(defn create []
  {
   :last (get-current)
   :ranges []
   :sum 0})

(defn started? [timer]
  (not= nil (:last timer)))

(defn start! [timer]
  (swap! timer start))

(defn stop! [timer]
  (swap! timer stop))

(defn create! [timer]
  (reset! timer (create)))
