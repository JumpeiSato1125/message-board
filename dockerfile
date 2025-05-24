# Java 17 のベースイメージ
FROM eclipse-temurin:17-jdk

# 作業ディレクトリ
WORKDIR /app

# jar ファイルをコピー（ファイル名は変更可能）
COPY target/message-board-0.0.1-SNAPSHOT.jar app.jar

# ポートを指定
EXPOSE 8080

# アプリ起動
ENTRYPOINT ["java", "-jar", "app.jar"]