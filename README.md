# MAMBot

Provided is a functional proof of concept of a headless Monster & Me 2.5 client. This code is the conclusion to many years of playing and reverse engineering Monster & Me. This project itself was written in the span of 3 weeks as a project to learn Java and get a functional packet client and bot finally working.

Provided is code for packet encryption/decrpytion, many classes for game objects, packet structure classes, a class for path finding/moving maps, a packet event system, a class for leveling a master god from 1-800 (takes about 25 minutes), a rudimentary class to intercept game messages to execute functions within the bot, an expiremental class that gathers Hunting Drugs, packet structure class, and socket with built in encryption/decryption using Monster and Me's encryption/decription algorithms

A note on passwords: I had trouble reverse engineering the password encryption algorithm that is used to encrypt your password before sending it to the server where it gets decrypted (MAM stores passwords in plain text, as proven by leaked private server from TQ Digital. For shame!). I believe it's using RC5, but had no luck using traditional libraries. I may have been using them incorrectly at the time. Instead of wasting time fighting it, I used IDA to export the assembly into functional C code. As the first client I wrote was using C/C++, this was perfectly fine. Since this client is in Java, that is a problem. That's where libMAMPassword came in. It's a library that has one method, encryptPassword(...). Included are Java bindings, but they need to be compiled in a certain way. Which I've long since forgotten, and apparently didn't commit.

There's a lot of useful code that can be easily applied to the Chinese servers of Monster & Me, if someone wanted to put the time into it. At the time of writing this code (2012) this was the only packet client in existance, to my knowledge. And I kept it to myself. It is now open source, and anyone is free to do what they will with it. I only ask to be credited, where credidt is due. See also, the [License file](License.md).

This code is presented AS-IS. It may or may not compile. I know there's some stuff missing from the Cpp directory for libMAMPassword. The Java code depends on a map file to exist. This is the same map grah format that HLHL and Genius use. So they can just be put into the correct folder (check the source). This code was intended for use on GNU/Linux or OS X. I believe I had it running on OS X as well as Ubuntu.

May the force be with you.

## License

The MIT License (MIT). Please see [License File](LICENSE.md) for more information.
