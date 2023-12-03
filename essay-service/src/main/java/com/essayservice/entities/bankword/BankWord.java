package com.essayservice.entities.bankword;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("bankWord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankWord {
    @Id
    private Object id;
    private String word;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankWord bankWord = (BankWord) o;
        return Objects.equals(word, bankWord.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
