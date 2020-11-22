## 首相動静クローラー
TBW

```
# UTF-8 & 改行コードの変換
$ nkf -w -Lu --overwrite original/*
```

## Link
- [検索＆ダウンロード \| 総理、きのう何してた？ \| NHK政治マガジン](https://www.nhk.or.jp/politics/souri/search/index.html)

### scala.util.Using

- [Scala Standard Library 2\.13\.4 \- scala\.util\.Using](https://www.scala-lang.org/api/current/scala/util/Using$.html)
- https://qiita.com/ka2kama/items/cd846b15fbb56cdbc9ea
- https://medium.com/@dkomanov/scala-try-with-resources-735baad0fd7d

### Overload constructor for Scala's Case Classes?

```scala 
case class Foo(bar: Int, baz: Int) {
    def this(bar: Int) = this(bar, 0)
}

new Foo(1, 2)
new Foo(1)
```
https://stackoverflow.com/questions/2400794/overload-constructor-for-scalas-case-classes
