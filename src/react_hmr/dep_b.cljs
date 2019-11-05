(ns react-hmr.dep-b
  (:require [react-hmr.dep-c :as c]))

(defn greet [s]
  (str c/greeting s "!"))
