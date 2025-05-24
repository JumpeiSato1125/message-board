# ベースイメージを指定（Java 17）
FROM eclipse-temurin:17-jdk

# 作業ディレクトリ
WORKDIR /app

# jarファイルをコピー（ファイル名は要調整）
COPY target/message-board-0.0.1-SNAPSHOT.jar app.jar

# ポートを指定（Render側でも設定必要）
EXPOSE 8080

# 起動コマンド
ENTRYPOINT ["java", "-jar", "app.jar"]