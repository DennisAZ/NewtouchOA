{
  "rtState": "0", "rtMsrg": "", "rtData": {
    "currency": [{
      "currency": "RMB",
      "rate": 1
    }, {
      "currency": "USD",
      "rate": 6.2324
    }],
    "summary":  [
      "天龙八部",
      "test",
      "1001",
      "1002",
      "1120",
      "C",
      "C++",
      "Clojure",
      "COBOL",
      "ColdFusion",
      "Erlang",
      "Fortran",
      "Groovy",
      "Haskell",
      "Java",
      "JavaScript",
      "Lisp",
      "Perl",
      "PHP",
      "Python",
      "Ruby",
      "Scala",
      "Scheme"
    ],
    "items": {
      "9988": {
        "id": "9988",
        "elements": [{
          "text": "金额",
          "type": "price",
          "id": "price",
          "name": "price",
          "unit": "",
          "viewInGrid": true
        }, {
          "text": "数量",
          "type": "amount",
          "id": "count",
          "name": "count",
          "unit": "个",
          "viewInGrid": true
        }]
      },
      "6699": {
        "id": "9988",
        "elements": [{
          "text": "单价",
          "type": "price",
          "id": "price",
          "name": "price",
          "unit": ""
        }, {
          "text": "数量",
          "type": "amount",
          "id": "amount",
          "name": "amount",
          "unit": "台"
        }, {
          "text": "总额",
          "type": "price",
          "id": "count",
          "name": "price",
          "value": "${price}*${amount}",
          "attributes": {
            "disabled": true
          },
          "unit": "台",
          "viewInGrid": true
        }]
      }
    }
  }
}