(ns react-hmr.react
  (:require ["react" :as react]))

(defn $
  [type & args]
  (let [type' (if (keyword? type)
                (name type)
                type)]
    (if (map? (first args))
      (apply react/createElement type' (clj->js (first args)) (rest args))
      (apply react/createElement type' nil args))))


;; Make printing React elements work
(when (exists? js/Symbol)
  (extend-protocol IPrintWithWriter
    js/Symbol
    (-pr-writer [sym writer _]
      (-write writer (str "\"" (.toString sym) "\"")))))
