## 首相動静クローラー
TBW

```shell
# 今月の首相動静CSVを取得
$ ./sbt crawl

# 指定月(2020年12月)の首相動静CSVを取得
$ ./sbt "crawl 202012"
```

[comment]: <> (see: [Linux 【 nkf, iconv 】 文字＆改行コード変換 \- Qiita]&#40;https://qiita.com/r18j21/items/78d8501888839b13c770&#41;)

### Install java

```
$ asdf install java adoptopenjdk-11.0.7+10.1

$ asdf current java
java            adoptopenjdk-11.0.7+10.1 /Users/yuokada/PycharmProjects/Prime-Minister-movements/.tool-versions
```

### Run a converter script

```
$ ./sbt run
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
