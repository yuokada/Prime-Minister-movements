## 首相動静クローラー

**The web site, NHK政治マガジン, has been shutdown.**

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
