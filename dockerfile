# Java 17 の公式 JDK イメージを使う
FROM eclipse-temurin:17-jdk

# 作業ディレクトリ作成
WORKDIR /app

# JAR ファイルをコピー（`mvn package` 後の生成物）
COPY target/message-board-0.0.1-SNAPSHOT.jar app.jar

# アプリが使用するポート
EXPOSE 8080

# アプリの起動コマンド
ENTRYPOINT ["java", "-jar", "app.jar"]